package org.bluej.delta.client.compile;


/**
 * A message from the compiler. Either an error or a warning.
 * @author Poul Henriksen
 *
 */
public class CompileMessage
{
    private String type;
    private String message;
    private int lineNumber;
	private int columnNumber;

    public static final String WARNING = "WARNING";
    public static final String ERROR = "ERROR";
    
    public CompileMessage(String type, String message, int lineNumber, int columnNumber) {
        this.type = type;
        this.message = message;
        this.lineNumber = lineNumber;
		this.columnNumber = columnNumber;
    }

    public int getLineNumber()
    {
        return lineNumber;
    }

    public int getColumnNumber()
    {
        return columnNumber;
    }

    public String getMessage()
    {
        return message;
    }

    public String getType()
    {
        return type;
    }
}
