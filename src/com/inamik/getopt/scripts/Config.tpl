{{if $configPackage}}package {{$configPackage}};{{/}}

import java.util.List;
import java.util.ArrayList;
import java.io.File;

/**
 * {{$configClass}}
 * Built using iNamik GetOpt
 */
public final class {{$configClass}}
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

	{{ foreach id=option in=$options }}
		{{ if $option.type != "help" }}
			// {{$option.name}}
			private {{$option.javaType}} {{$option.javaName}} = null;
			{{br}}
		{{/}}
	{{/}}

	/**
	 * Constructor
	 */
	public {{$configClass}}()
	{
		super();
		{{ foreach id=option in=$options }}
			{{ if $option.type != "help" && $option.default && $option.default != "null" }}
				{{br}}
				// {{$option.name}}
				this.{{$option.javaName}} = {{$option.default}};
			{{/}}
		{{/}}
	}

	/**
	 * validate
	 */
	public void validate() throws ValidationException
	{
		List errors = new ArrayList();

		{{set $br = false}}
		{{ foreach id=option in=$options }}
			{{ if $option.type != "help" }}
				{{ if $option.required }}
					{{ if $br }}{{br}}{{else}}{{set $br = true}}{{/}}
					// {{$option.name}} - Required
					if (this.{{$option.javaName}} == null)
					{
						errors.add("Missing Parameter: {{$option.javaName}}");
					}
				{{/}}
				{{ if $option.type == "input-file" }}
					{{ if $br }}{{br}}{{else}}{{set $br = true}}{{/}}
					{{ if $option.required }}{{br}}{{/}}
					// {{$option.name}} - Verify File Exists
					if	(
							(this.{{$option.javaName}} != null)
						&&	({{$configClass}}.fileExists(this.{{$option.javaName}}) == false)
						)
					{
						errors.add("File does not exist: '" + this.{{$option.javaName}} + "'");
					}
				{{ elseif $option.type == "input-dir" }}
					{{ if $br }}{{br}}{{else}}{{set $br = true}}{{/}}
					{{ if $option.required }}{{br}}{{/}}
					// {{$option.name}} - Verify Directory Exists
					if	(
							(this.{{$option.javaName}} != null)
						&&	({{$configClass}}.dirExists(this.{{$option.javaName}}) == false)
						)
					{
						errors.add("Directory does not exist: '" + this.{{$option.javaName}} + "'");
					}
				{{/}}
			{{/}}
		{{/}}
		{{ if $br }}{{br}}{{else}}{{set $br = true}}{{/}}
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

	{{set $br=false }}
	{{ foreach id=option in=$options }}
		{{ if $option.type != "help" }}
			{{ if $br == true }}{{br}}{{else}}{{set $br = true}}{{/}}
			/**
			 * {{$option.getter}}
			 */
			public {{$option.javaType}} {{$option.getter}}()
			{
				return this.{{$option.javaName}};
			}

			/**
			 * {{$option.setter}}
			 */
			public void {{$option.setter}}({{$option.javaType}} {{$option.javaName}})
			{
				this.{{$option.javaName}} = {{$option.javaName}};
			}
		{{/}}
	{{/}}
}