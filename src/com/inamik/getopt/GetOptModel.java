/*
 * $Id: $
 *
 * iNamik GetOpt Generator
 * Copyright (C) 2008 David Farrell (davidpfarrell@yahoo.com)
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package com.inamik.getopt;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import com.inamik.template.*;
import com.inamik.template.lib.xml.util.XMLUtil;
import com.inamik.template.lib.xml.util.XPathUtil;

/**
 * Model
 */
public final class GetOptModel
{
	public static final class Option
	{
		private String  name;
		private String  shortName;
		private String  type;
		private Boolean required;
		private Integer minOccurs;
		private Integer maxOccurs;
		private String  defaultValue;
		private String  javaName;
		private String  dashName;
		private String  getter;
		private String  setter;
		private String  javaType;
		public String getName()       { return name;         }
		public String getShort()      { return shortName;    }
		public String getType()       { return type;         }
		public Boolean getRequired()  { return required;     }
		public Integer getMinOccurs() { return minOccurs;    }
		public Integer getMaxOccurs() { return maxOccurs;    }
		public String getDefault()    { return defaultValue; }
		public String getJavaName()   { return javaName;     }
		public String getDashName()   { return dashName;     }
		public String getGetter()     { return getter;       }
		public String getSetter()     { return setter;       }
		public String getJavaType()   { return javaType;     }
	}

	private GetOptConfig config;

	/**
	 * Default Constructor (private)
	 */
	private GetOptModel()
	{
		super();
	}

	/**
	 * Constructor
	 */
	public GetOptModel(GetOptConfig config)
	{
		this();

		this.config = config;
	}

	public void execute() throws Exception
	{
		// Validate the config
		config.validate();

		// Load the xmlFile
		final org.w3c.dom.Document document = XMLUtil.getDocument(config.getXmlFile());

		if (document == null)
		{
			throw new Exception("Unable to load xml file '" + config.getXmlFile() + "'");
		}

		// Get the option nodes
		final List optionNodes = XPathUtil.evaluateXPathAsList("/getopt/options/option", document);

		final List<Option> options = new ArrayList<Option>(optionNodes.size());

		// Help
		options.add(getOption("help", "?", "help", "no", "0", "1", null));

		// Cycle through the nodes, computing the Option object
		for (Object optionNode : optionNodes)
		{
			options.add(getOption(optionNode));
		}

		// Get javaDir
		File javaDir = new File (config.getJavaDir());

		if (javaDir.isDirectory() == false)
		{
			throw new Exception("'" + config.getJavaDir() + "' is not a directory");
		}

		// Compute some globals
		final String mainPackage       = getJavaPackageNameFromFQName(config.getMainClass());
		final String mainClass         = getJavaClassNameFromFQName(config.getMainClass());
		final String mainPackagePath   = getJavaPackagePathFromPackageName(mainPackage);
		final String configPackage     = getJavaPackageNameFromFQName(config.getConfigClass());
		final String configClass       = getJavaClassNameFromFQName(config.getConfigClass());
		final String configPackagePath = getJavaPackagePathFromPackageName(configPackage);

		TemplateEngine templateEngine = TemplateEngine.getInstance();

		// Get the 'main' template
		Template mainTemplate = templateEngine.loadTemplateFromResource("com/inamik/getopt/scripts/Main.tpl");

		File mainPackageDir = new File(javaDir, mainPackagePath);

		if (mainPackageDir.isDirectory() == false && mainPackageDir.mkdirs() == false)
		{
			throw new Exception("Unable to create directory '" + mainPackageDir.getAbsolutePath() + "'");
		}

		File mainFile = new File(mainPackageDir, mainClass + ".java");

		PrintWriter mainWriter = new PrintWriter(mainFile);
		mainTemplate.setOut(mainWriter);

		mainTemplate.addVariable("options"      , options);
		mainTemplate.addVariable("exeClass"     , config.getExeClass());
		mainTemplate.addVariable("mainPackage"  , mainPackage);
		mainTemplate.addVariable("mainClass"    , mainClass);
		mainTemplate.addVariable("configPackage", configPackage);
		mainTemplate.addVariable("configClass"  , configClass);

		mainTemplate.process();

		// Get the 'config' template
		Template configTemplate = templateEngine.loadTemplateFromResource("com/inamik/getopt/scripts/Config.tpl");

		File configPackageDir = new File(javaDir, configPackagePath);

		if (configPackageDir.isDirectory() == false && configPackageDir.mkdirs() == false)
		{
			throw new Exception("Unable to create directory '" + configPackageDir.getAbsolutePath() + "'");
		}

		File configFile = new File(configPackageDir, configClass + ".java");

		PrintWriter configWriter = new PrintWriter(configFile);
		configTemplate.setOut(configWriter);

		configTemplate.addVariable("options"      , options);
		configTemplate.addVariable("exeClass"     , config.getExeClass());
		configTemplate.addVariable("mainPackage"  , mainPackage);
		configTemplate.addVariable("mainClass"    , mainClass);
		configTemplate.addVariable("configPackage", configPackage);
		configTemplate.addVariable("configClass"  , configClass);

		configTemplate.process();
	}

	private Option getOption(Object optionNode) throws Exception
	{
		return getOption
		(
			XPathUtil.evaluateXPathAsString("@name"     , optionNode),
			XPathUtil.evaluateXPathAsString("@short"    , optionNode),
			XPathUtil.evaluateXPathAsString("@type"     , optionNode),
			XPathUtil.evaluateXPathAsString("@required" , optionNode),
			XPathUtil.evaluateXPathAsString("@minOccurs", optionNode),
			XPathUtil.evaluateXPathAsString("@maxOccurs", optionNode),
			XPathUtil.evaluateXPathAsString("@default"  , optionNode)
		);
	}

	private Option getOption
	(
		String name,
		String shortName,
		String type,
		String required,
		String minOccurs,
		String maxOccurs,
		String defaultValue
	) throws Exception
	{
		Option option = new Option();

		if (name == null || name.length() == 0)
		{
			throw new Exception("Option missing name");
		}

		if (type == null || type.length() == 0)
		{
			throw new Exception("Option " + name + " missing type");
		}

		option.type = type;

		// Normalize the name
		name = name.replace(' ', '_').replace('-', '_');

		option.name = name;

		option.javaName = getJavaIdName(name);
		option.dashName = name.replace('_', '-');
		option.getter   = getJavaMethodName(name, "get");
		option.setter   = getJavaMethodName(name, "set");

		if (shortName == null || shortName.length() == 0)
		{
			shortName = null;
		}

		option.shortName = shortName;

		if (minOccurs != null && minOccurs.length() > 0)
		{
			option.minOccurs = Integer.valueOf(minOccurs);
		}

		if (
				(required != null)
			&&	(
					(required.equalsIgnoreCase("yes"))
				||	(required.equalsIgnoreCase("true"))
				||	(required.equalsIgnoreCase("1"))
				)
			)
		{
			option.required  = Boolean.TRUE;

			if (option.minOccurs == null)
			{
				option.minOccurs = Integer.valueOf(1);
			}
			else
			if (option.minOccurs.intValue() < 1)
			{
				throw new Exception("option " + name + ": minOccurs cannot be less than 1 if option is required");
			}
		}
		else
		{
			option.required = Boolean.FALSE;

			if (option.minOccurs == null)
			{
				option.minOccurs = Integer.valueOf(0);
			}
			else
			if (option.minOccurs.intValue() > 0)
			{
				throw new Exception("option " + name + ": minOccurs cannot be greater than 0 if option is not required");
			}
		}

		if (maxOccurs != null && maxOccurs.length() > 0)
		{
			if (maxOccurs.equalsIgnoreCase("unbounded"))
			{
				option.maxOccurs = Integer.valueOf(0);
			}
			else
			{
				option.maxOccurs = Integer.valueOf(maxOccurs);

				if (option.maxOccurs.intValue() < option.minOccurs.intValue())
				{
					throw new Exception("option " + name + ": maxOccurs cannot be less than minOccurs");
				}
			}
		}
		else
		{
			if (option.minOccurs.intValue() > 0)
			{
				option.maxOccurs = option.minOccurs;
			}
			else
			{
				option.maxOccurs = Integer.valueOf(1);
			}
		}

		if (defaultValue == null || defaultValue.length() == 0)
		{
			defaultValue = null;
		}

		if (type.equalsIgnoreCase("boolean"))
		{
			option.javaType = "Boolean";

			if (defaultValue != null && defaultValue.equalsIgnoreCase("true"))
			{
				option.defaultValue = "Boolean.TRUE";
			}
			else
			{
				option.defaultValue = "Boolean.FALSE";
			}
		}
		else
		if (type.equalsIgnoreCase("integer"))
		{
			option.javaType = "Integer";

			if (defaultValue != null && defaultValue.length() > 0)
			{
				option.defaultValue = "Integer.valueOf(" + defaultValue + ")";
			}
			else
			{
				option.defaultValue = "null";
			}
		}
		else
		if	(
				(type.equalsIgnoreCase("string"     ))
			||	(type.equalsIgnoreCase("input-file" ))
			||	(type.equalsIgnoreCase("output-file"))
			||	(type.equalsIgnoreCase("input-dir"  ))
			||	(type.equalsIgnoreCase("output-dir" ))
			)
		{
			option.javaType = "String";

			if (defaultValue != null)
			{
				option.defaultValue = '"' + defaultValue + '"';
			}
			else
			{
				option.defaultValue = "null";
			}
		}
		else
		if (type.equalsIgnoreCase("help") == false)
		{
			throw new Exception("option " + name + ": Unknown Option Type '" + type + "'");
		}

		option.defaultValue = defaultValue;

		return option;
	}

	private String getJavaIdName(final String name)
	{
		return getJavaIdName(name, null);
	}

	private String getJavaIdName(final String name, final String prefix)
	{
		char[] charArray = name.toLowerCase().toCharArray();

		StringBuffer stringBuffer = new StringBuffer();

		// first char is lower case in id name
		boolean upperCase = false;

		if (prefix != null)
		{
			stringBuffer.append(prefix.toLowerCase());
			upperCase = true;
		}

		for (int iFig = 0; iFig < charArray.length; ++iFig)
		{
			char nextChar = charArray[iFig];

			// Ignore '$' & "_", even though they are valid java identifiers
			// Make sure character is valid for start/part
			if	(
					((nextChar != '$') && (nextChar != '_'))
				&&	(
						(
							(stringBuffer.length() == 0)
						&&	(Character.isJavaIdentifierStart(nextChar))
						)
					||	(
							(stringBuffer.length() > 0)
						&&	(Character.isJavaIdentifierPart(nextChar))
						)
					)
				)
			{
				if (upperCase == true)
				{
					nextChar = Character.toUpperCase(nextChar);
				}

				stringBuffer.append(nextChar);

				upperCase = false;
			}
			else if ((nextChar == ' ') || (nextChar == '_') || (nextChar == '-'))
			{
				upperCase = true;
			}
		}

		return stringBuffer.toString();
	}

	private String getJavaMethodName(final String name, final String prefix)
	{
		char[] charArray = name.toLowerCase().toCharArray();

		StringBuffer stringBuffer = new StringBuffer();

		// first char is lower case in id name
		boolean upperCase = false;

		if (prefix != null)
		{
			stringBuffer.append(prefix.toLowerCase());
			upperCase = true;
		}

		for (int iFig = 0; iFig < charArray.length; ++iFig)
		{
			char nextChar = charArray[iFig];

			// Ignore '$' & "_", even though they are valid java identifiers
			// Make sure character is valid for start/part
			if	(
					((nextChar != '$') && (nextChar != '_'))
				&&	(
						(
							(stringBuffer.length() == 0)
						&&	(Character.isJavaIdentifierStart(nextChar))
						)
					||	(
							(stringBuffer.length() > 0)
						&&	(Character.isJavaIdentifierPart(nextChar))
						)
					)
				)
			{
				if (upperCase == true)
				{
					nextChar = Character.toUpperCase(nextChar);
				}

				stringBuffer.append(nextChar);

				upperCase = false;
			}
			else if ((nextChar == ' ') || (nextChar == '_') || (nextChar == '-'))
			{
				upperCase = true;
			}
		}

		return stringBuffer.toString();
	}

	private String getJavaPackageNameFromFQName(final String name)
	{
		int index = name.lastIndexOf('.');

		if (index >= 0)
		{
			return name.substring(0, index);
		}
		else
		{
			return "";
		}
	}

	private String getJavaClassNameFromFQName(final String name)
	{
		int index = name.lastIndexOf('.');

		if (index >= 0)
		{
			return name.substring(index + 1);
		}
		else
		{
			return name;
		}
	}

	private String getJavaPackagePathFromPackageName(final String packageName)
	{
		return packageName.replace('.', '/');
	}

	private String getJavaClassName(final String name)
	{
		char[] charArray = name.toLowerCase().toCharArray();

		StringBuffer stringBuffer = new StringBuffer();

		// first char is Uppercase in class name
		boolean upperCase = true;

		for (int iFig = 0; iFig < charArray.length; ++iFig)
		{
			char nextChar = charArray[iFig];

			// Ignore '$' & "_", even though they are valid java identifiers
			// Make sure character is valid for start/part
			if	(
					((nextChar != '$') && (nextChar != '_'))
				&&	(
						(
							(stringBuffer.length() == 0)
						&&	(Character.isJavaIdentifierStart(nextChar))
						)
					||	(
							(stringBuffer.length() > 0)
						&&	(Character.isJavaIdentifierPart(nextChar))
						)
					)
				)
			{
				if (upperCase == true)
				{
					nextChar = Character.toUpperCase(nextChar);
				}

				stringBuffer.append(nextChar);

				upperCase = false;
			}
			else if ((nextChar == ' ') || (nextChar == '_') || (nextChar == '-'))
			{
				upperCase = true;
			}
		}

		return stringBuffer.toString();
	}
}
