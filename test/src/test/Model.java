package test;

/**
 * Model
 */
public final class Model
{
	private GetOptConfig config;

	/**
	 * Default Constructor (private)
	 */
	private Model()
	{
		super();
	}

	/**
	 * Constructor
	 */
	public Model(GetOptConfig config)
	{
		this();

		this.config = config;
	}

	public void execute() throws Exception
	{
		// Validate the config
		config.validate();

		System.out.println("You did it!");

	}
}
