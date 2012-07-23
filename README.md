Java GetOpt Generator
=====================

**Generate a command-line-argument parser & config class from an XML description file**

DESCRIPTION
-----------

Using an XML file that describes your command-line options, iNamik GetOpt Generator
will generate a 'parser' class capable of reading options from the command line, as
well as a 'config' class that stores the processed command-line options.

Having a 'config' class allows your application to be programmatically executed from
oher Java applications.


SELF-HOSTING
------------

GetOpt Generator is Self-Hosting, meaning it uses itself to generate its command-line
parser.


XML CONFIGURATION
-----------------

*Example XML*

'''
<?xml version="1.0" encoding="UTF-8"?>
<!-- This is the actual XML configuration used to build GetOpt Generator -->
<getopt>
	<options>
		<!-- xml file -->
		<option name="xml-file" short="x" type="input-file" required="yes"/>

		<!-- java dir -->
		<option name="java dir" short="j" type="output-dir" required="yes"/>

		<!-- exe class -->
		<option name="exe_class" short="e" type="string" required="yes"/>

		<!-- main class -->
		<option name="main-class" short="m" type="string" required="yes"/>

		<!-- config class -->
		<option name="config class" short="c" type="string" required="yes"/>
	</options>
</getopt>
'''

*Descriptions*

	*name*

		Long name of the option. Separate logical words with "_"
		(i.e. long_opt)

	*short*

		Short (1 char) alias for the option

	*type*

		One of:

		* boolean
		* integer
		* string
		* input-file  (verifies that file exists)
		* input-dir   (verifies that dir exists)
		* output-file (treated as string for now)
		* output-dir  (treated as string for now)

	*required*

		yes | no

	*default*

		A default value appropriate for the type


RUNNING FROM COMMAND-LINE
-------------------------

Running GetOpt with `--help` generates the following output

'''
options for GetOpt :

        [ --help | -? ]
        ( --xmlFile | -x ) xml_file
        ( --javaDir | -j ) java_dir
        ( --exeClass | -e ) exe_class
        ( --mainClass | -m ) main_class
        ( --configClass | -c ) config_class

NOTE: Options in brackets '[]' are optional.
      All camel-case long opts can be represented using lowercase,
      as well as with '-' or '_' seperating the camel-cased words
      (i.e. --longOpt | --longopt | --long_opt | --long-opt)
'''

*xml_file*

The XML file containing the configuration

*main_class*

The fully qualified name (package+class) of the generated class that will parse
the command line (i.e. contains the `main()` method).

*config_class*

The fully qualified name (package_class) of the generated class that will store
the config information.

*java_dir*

The base output directory (i.e. your src directory).  The generated files will be
stored in `/java/dir/package/path/ClassName.java`

*exe_class*

The Java class that the generated command-line parser should call after parsing
the command line.

The exe class is expected to contain a constructor that accepts a `config_class`
instance.

The exe class is expected to contain an `execute()` method, which will be
called by the command-line parser.

It is the responsibility of the exe class to validate the parsed config, but the
`config_class` is generated with a validate() function which does the heavy
lifting.


REQUIREMENTS
------------

This project has the following dependencies:

	* iNamik Template Engine (0.63.3a)
	* iNamik Template Lib - XML (0.50.1)
	* Activation (1.1.1)
	* ANTLR (2.7.7)
	* Commons Beanutils (1.8.3) Core
	* Commons Logging (1.1.1)
	* EHCache (2.2.0) Core
	* JAXB API (2.2.1 20100511)
	* JAXB IMPL (2.2.1 20100511)
	* Jaxen (1.1.4)
	* JSR173 API (1.0)


DOWNLOAD
--------

* View the source code on [GitHub](https://github.com/iNamik/Java-GetOpt-Generator)
* Download [Source/Binary Distributions](https://github.com/iNamik/Java-GetOpt-Generator/downloads)


AUTHORS
-------

 * David Farrell
