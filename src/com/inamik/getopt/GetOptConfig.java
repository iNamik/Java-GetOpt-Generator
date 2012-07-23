package com.inamik.getopt;

import java.util.List;
import java.util.ArrayList;
import java.io.File;

/**
 * GetOptConfig
 * Built using iNamik GetOpt
 */
public final class GetOptConfig
{
	/**
	 * ValidationException
	 */
	public static final class ValidationException extends Exception
	{
		private List errors = null;

		public ValidationException(List errors)
		{
			super();
			this.errors = errors;
		}
		public List getErrors() {return this.errors;}
	}

	// Operands
	private List operands = new ArrayList();

	// xml_file
	private String xmlFile = null;

	// java_dir
	private String javaDir = null;

	// exe_class
	private String exeClass = null;

	// main_class
	private String mainClass = null;

	// config_class
	private String configClass = null;

	/**
	 * Constructor
	 */
	public GetOptConfig()
	{
		super();
	}

	/**
	 * validate
	 */
	public void validate() throws ValidationException
	{
		List errors = new ArrayList();

		// xml_file - Required
		if (this.xmlFile == null)
		{
			errors.add("Missing Parameter: xmlFile");
		}

		// xml_file - Verify File Exists
		if	(
				(this.xmlFile != null)
			&&	(GetOptConfig.fileExists(this.xmlFile) == false)
			)
		{
			errors.add("File does not exist: '" + this.xmlFile + "'");
		}

		// java_dir - Required
		if (this.javaDir == null)
		{
			errors.add("Missing Parameter: javaDir");
		}

		// exe_class - Required
		if (this.exeClass == null)
		{
			errors.add("Missing Parameter: exeClass");
		}

		// main_class - Required
		if (this.mainClass == null)
		{
			errors.add("Missing Parameter: mainClass");
		}

		// config_class - Required
		if (this.configClass == null)
		{
			errors.add("Missing Parameter: configClass");
		}

		if (errors.size() > 0)
		{
			throw new ValidationException(errors);
		}
	}

	/**
	 * fileExists
	 */
	public static boolean fileExists(String filename)
	{
		File file = new File(filename);

		return file.exists();
	}

	/**
	 * dirExists
	 */
	public static boolean dirExists(String dirname)
	{
		File dir = new File(dirname);

		return dir.isDirectory();
	}

	/**
	 * getOperands
	 */
	public List getOperands()
	{
		return new ArrayList(operands);
	}

	/**
	 * addOperand
	 */
	public void addOperand(String operand)
	{
		operands.add(operand);
	}

	/**
	 * getXmlFile
	 */
	public String getXmlFile()
	{
		return this.xmlFile;
	}

	/**
	 * setXmlFile
	 */
	public void setXmlFile(String xmlFile)
	{
		this.xmlFile = xmlFile;
	}

	/**
	 * getJavaDir
	 */
	public String getJavaDir()
	{
		return this.javaDir;
	}

	/**
	 * setJavaDir
	 */
	public void setJavaDir(String javaDir)
	{
		this.javaDir = javaDir;
	}

	/**
	 * getExeClass
	 */
	public String getExeClass()
	{
		return this.exeClass;
	}

	/**
	 * setExeClass
	 */
	public void setExeClass(String exeClass)
	{
		this.exeClass = exeClass;
	}

	/**
	 * getMainClass
	 */
	public String getMainClass()
	{
		return this.mainClass;
	}

	/**
	 * setMainClass
	 */
	public void setMainClass(String mainClass)
	{
		this.mainClass = mainClass;
	}

	/**
	 * getConfigClass
	 */
	public String getConfigClass()
	{
		return this.configClass;
	}

	/**
	 * setConfigClass
	 */
	public void setConfigClass(String configClass)
	{
		this.configClass = configClass;
	}
}