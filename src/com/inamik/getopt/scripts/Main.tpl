{{if $mainPackage}}package {{$mainPackage}};{{/}}

import {{ if $configPackage}}{{$configPackage}}.{{/}}{{$configClass}};
import {{ if $configPackage}}{{$configPackage}}.{{/}}{{$configClass}}.ValidationException;

import java.util.List;
import java.util.ArrayList;

/**
 * {{$mainClass}}
 * Built using iNamik GetOpt Generator
 */
public final class {{$mainClass}}
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
	private {{$configClass}} config = null;

	/**
	 * Defult Constructor (private)
	 */
	private {{$mainClass}}()
	{
		super();
	}

	/**
	 * Constructor
	 */
	public {{$mainClass}}({{$configClass}} config)
	{
		this();
		this.config = config;
	}

	/**
	 * main
	 */
	public static void main(String[] args)
	{
		{{$mainClass}} main = new {{$mainClass}}(new {{$configClass}}());

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
				{{$exeClass}} exe = new {{$exeClass}}(main.config);
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
		System.out.println("options for {{$mainClass}} :\n");

		{{ foreach id=option in=$options }}
			{{ set $line = "--{$option.javaName}" }}
			{{ if $option.short }}
				{{ set $line = "{$line} | -{$option.short}" }}
			{{/}}
			{{ unless ($option.type == "boolean" || $option.type == "help") }}
				{{ set $operand = lower($option.name) }}
				{{ if $option.short }}
					{{ set $line = "( {$line} )" }}
				{{/}}
				{{ set $line = "{$line} {$operand}" }}
			{{/}}
			{{ unless $option.required }}
				{{ set $line = "[ {$line} ]" }}
			{{/}}
			System.out.println("\t{{$line}}");
		{{/}}
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

		{{ foreach id=option in=$options loop=loop }}
			{{ if !$loop.first }}
				else
			{{/}}
			// {{$option.name}}
			if	(
					(option.equalsIgnoreCase("{{$option.name}}"))
				{{ if $option.name != $option.javaName }}
				||	(option.equalsIgnoreCase("{{$option.javaName}}"))
				{{/}}
				{{ if $option.name != $option.dashName }}
				||	(option.equalsIgnoreCase("{{$option.dashName}}"))
				{{/}}
				)
			{
				{{ if $option.type == "help" }}
					this.help = true;
				{{ elseif $option.javaType == "String" }}
					config.{{$option.setter}}(getValueOrArg(value, argList));
				{{ elseif $option.javaType == "Integer" }}
					config.{{$option.setter}}(Integer.valueOf(getValueOrArg(value, argList)));
				{{ elseif $option.javaType == "Boolean" }}
					config.{{$option.setter}}(Boolean.TRUE);
				{{/}}
			}
		{{/}}
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

		{{ set $printElse = false }}
		{{ foreach id=option in=$options }}
			{{ if $option.short }}
				{{ if $printElse }}
					else
				{{ else }}
					{{ set $printElse = true }}
				{{/}}
				// {{$option.name}}
				if (option.equals("{{$option.short}}"))
				{
					{{ if $option.type == "help" }}
						this.help  = true;
						reuseValue = true;
					{{ elseif $option.javaType == "String" }}
						config.{{$option.setter}}(getValueOrArg(value, argList));
					{{ elseif $option.javaType == "Integer" }}
						config.{{$option.setter}}(Integer.valueOf(getValueOrArg(value, argList)));
					{{ elseif $option.javaType == "Boolean" }}
						config.{{$option.setter}}(Boolean.TRUE);
						reuseValue = true;
					{{/}}
				}
			{{/}}
		{{/}}
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