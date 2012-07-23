package com.inamik.getopt;

import com.inamik.getopt.GetOptConfig;
import com.inamik.getopt.GetOptConfig.ValidationException;

import java.util.List;
import java.util.ArrayList;

/**
 * GetOpt
 * Built using iNamik GetOpt Generator
 */
public final class GetOpt
{
	/**
	 * UnrecognizedOptionException
	 */
	public static final class UnrecognizedOptionException extends Exception
	{
		private String option = null;
		public UnrecognizedOptionException(String option)
		{
			super();
			this.option = option;
		}
		public String getOption() {return this.option;}
	}

	private boolean help = false;
	private GetOptConfig config = null;

	/**
	 * Defult Constructor (private)
	 */
	private GetOpt()
	{
		super();
	}

	/**
	 * Constructor
	 */
	public GetOpt(GetOptConfig config)
	{
		this();
		this.config = config;
	}

	/**
	 * main
	 */
	public static void main(String[] args)
	{
		GetOpt main = new GetOpt(new GetOptConfig());

		try
		{
			main.processArgs(args);
		}
		catch (UnrecognizedOptionException e)
		{
			System.err.println("Unrecognized Option : " + e.getOption());
			System.err.println("\n\nUse --help for help on running this utility\n");
			return;
		}

		if (main.help == true)
		{
			help();
		}
		else
		{
			try
			{
				GetOptModel exe = new GetOptModel(main.config);
				exe.execute();
			}
			catch (ValidationException ve)
			{
				List errors = ve.getErrors();

				for (int i = 0; i < errors.size(); ++i)
				{
					System.err.println((String)errors.get(i));
				}
				System.err.println("\nUse --help for help on running this utility\n");
				return;
			}
			catch (Exception e)
			{
				System.err.println(e.getMessage());
				return;
			}
		}
	}

	/**
	 * help
	 */
	public static void help()
	{
		System.out.println("options for GetOpt :\n");

		System.out.println("\t[ --help | -? ]");
		System.out.println("\t[ --help2 | -h ]");
		System.out.println("\t( --xmlFile | -x ) xml_file");
		System.out.println("\t( --javaDir | -j ) java_dir");
		System.out.println("\t( --exeClass | -e ) exe_class");
		System.out.println("\t( --mainClass | -m ) main_class");
		System.out.println("\t( --configClass | -c ) config_class");
		System.out.println
		(
			  "\n"
			+ "NOTE: Options in brackets '[]' are optional.\n"
			+ "      All camel-case long opts can be represented using lowercase,\n"
			+ "      as well as with '-' or '_' seperating the camel-cased words\n"
			+ "      (i.e. --longOpt | --longopt | --long_opt | --long-opt)"
		);
	}

	/**
	 * processArgs
	 */
	public void processArgs(String[] args) throws UnrecognizedOptionException
	{
		List argList = new ArrayList();

		for (int i = 0; i < args.length; ++i)
		{
			argList.add(args[i]);
		}

		while (argList.size() > 0)
		{
			String arg = (String)argList.remove(0);

			int numDashes = 0;

			if (arg.startsWith("--") == true)
			{
				numDashes = 2;
			}
			else
			if (arg.startsWith("-") == true)
			{
				numDashes = 1;
			}

			String option;

			if (numDashes > 0)
			{
				option = arg.substring(numDashes);
			}
			else
			{
				option = arg;
			}

			boolean match = false;

			if (numDashes == 2)
			{
				match = matchLongOption(option, argList);
			}
			else
			if (numDashes == 1)
			{
				match = matchLongOption(option, argList);

				if (match == false)
				{
					match = matchShortOption(option, argList);
				}
			}
			else
			{
				config.addOperand(option);
				match = true;
			}
		}
	}

	/**
	 * matchLongOption
	 */
	public boolean matchLongOption(String option, List argList)
	{
		boolean result = true;

		String value = null;

		int index = option.indexOf('=');

		if (index > 0)
		{
			value  = option.substring(index + 1);
			option = option.substring(0, index);
		}

		// help
		if	(
				(option.equalsIgnoreCase("help"))
			)
		{
			this.help = true;
		}
		else
		// help2
		if	(
				(option.equalsIgnoreCase("help2"))
			)
		{
			this.help = true;
		}
		else
		// xml_file
		if	(
				(option.equalsIgnoreCase("xml_file"))
			||	(option.equalsIgnoreCase("xmlFile"))
			||	(option.equalsIgnoreCase("xml-file"))
			)
		{
			config.setXmlFile(getValueOrArg(value, argList));
		}
		else
		// java_dir
		if	(
				(option.equalsIgnoreCase("java_dir"))
			||	(option.equalsIgnoreCase("javaDir"))
			||	(option.equalsIgnoreCase("java-dir"))
			)
		{
			config.setJavaDir(getValueOrArg(value, argList));
		}
		else
		// exe_class
		if	(
				(option.equalsIgnoreCase("exe_class"))
			||	(option.equalsIgnoreCase("exeClass"))
			||	(option.equalsIgnoreCase("exe-class"))
			)
		{
			config.setExeClass(getValueOrArg(value, argList));
		}
		else
		// main_class
		if	(
				(option.equalsIgnoreCase("main_class"))
			||	(option.equalsIgnoreCase("mainClass"))
			||	(option.equalsIgnoreCase("main-class"))
			)
		{
			config.setMainClass(getValueOrArg(value, argList));
		}
		else
		// config_class
		if	(
				(option.equalsIgnoreCase("config_class"))
			||	(option.equalsIgnoreCase("configClass"))
			||	(option.equalsIgnoreCase("config-class"))
			)
		{
			config.setConfigClass(getValueOrArg(value, argList));
		}
		// Unrecognized option
		else
		{
			result = false;
		}

		return result;
	}

	/**
	 * matchShortOption
	 */
	public boolean matchShortOption(String option, List argList)
	{
		boolean result = true;

		String value = null;

		boolean reuseValue = false;

		if (option.length() > 1)
		{
			value  = option.substring(1);
			option = option.substring(0, 1);
		}

		// help
		if (option.equals("?"))
		{
			this.help  = true;
			reuseValue = true;
		}
		else
		// help2
		if (option.equals("h"))
		{
			this.help  = true;
			reuseValue = true;
		}
		else
		// xml_file
		if (option.equals("x"))
		{
			config.setXmlFile(getValueOrArg(value, argList));
		}
		else
		// java_dir
		if (option.equals("j"))
		{
			config.setJavaDir(getValueOrArg(value, argList));
		}
		else
		// exe_class
		if (option.equals("e"))
		{
			config.setExeClass(getValueOrArg(value, argList));
		}
		else
		// main_class
		if (option.equals("m"))
		{
			config.setMainClass(getValueOrArg(value, argList));
		}
		else
		// config_class
		if (option.equals("c"))
		{
			config.setConfigClass(getValueOrArg(value, argList));
		}
		// Unrecognized option
		else
		{
			result = false;
		}

		if (reuseValue == true && value != null && value.length() > 0)
		{
			argList.add(0, "-" + value);
		}

		return result;
	}

	/**
	 * getValueOrArg
	 */
	public String getValueOrArg(String value, List argList)
	{
		if (value != null)
		{
			return value;
		}
		else
		{
			return (String)argList.remove(0);
		}
	}
}