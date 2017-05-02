package objects;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.KeyboardFocusManager;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * Class extending {@link javax.swing.JTextArea JTextArea} with the addition of
 * a default text within the component.
 * 
 * @version 1.4
 * @author V-ed
 */
public class Field extends JTextArea implements FocusListener {
	
	private String defaultText;
	private boolean isChanged = false;
	
	private Color JAVA_DEFAULT_COLOR;
	
	/**
	 * Creates a {@link javax.swing.JTextArea JTextArea} but with an addition:
	 * the ability to set a default text for this Field.
	 * 
	 * @param defaultText
	 *            Default text to be shown in the text area.
	 * @param isSigneLine
	 *            Determines whether this JTextArea should be a one liner
	 *            instead of a multiline text area.
	 */
	public Field(String defaultText, boolean isSingleLine){
		
		super(1, 12);
		
		JAVA_DEFAULT_COLOR = this.getForeground();
		
		this.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(Color.GRAY),
				BorderFactory.createEmptyBorder(2, 2, 2, 2)));
		
		this.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS,
				null);
		this.setFocusTraversalKeys(
				KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS, null);
		
		this.setVisualState(false);
		
		this.addFocusListener(this);
		
		if(defaultText != null)
			this.setDefaultText(defaultText);
		if(isSingleLine)
			getDocument().putProperty("filterNewlines", Boolean.TRUE);
		
	}
	
	/**
	 * Creates a {@link javax.swing.JTextArea JTextArea} with the bounds set as
	 * parameters, but with an addition: the ability to set a default text for
	 * this Field.
	 * 
	 * @param defaultText
	 *            Default text to be shown in the text area.
	 * @param isSigneLine
	 *            Determines whether this JTextArea should be a one liner
	 *            instead of a multiline text area.
	 */
	public Field(String defaultText, boolean isSingleLine, int width, int height){
		
		this(defaultText, isSingleLine);
		
		this.setPreferredSize(new Dimension(width, height));
		
	}
	
	/**
	 * Sets the visual of the current Field depending on the boolean parameter.
	 * <p>
	 * <b>true</b> : Default text visuals and remove the current text.
	 * <p>
	 * <b>false</b> : Italic gray text and the default Field's text is shown.
	 * Also sets this Field's {@link #setChangedState(boolean)} to
	 * <code>false</code>.
	 */
	public void setVisualState(boolean isFocused){
		
		if(isFocused){
			this.setForeground(JAVA_DEFAULT_COLOR);
			this.setFont(this.getFont().deriveFont(Font.PLAIN));
			super.setText("");
		}
		else{
			this.setForeground(Color.LIGHT_GRAY);
			this.setFont(this.getFont().deriveFont(Font.ITALIC));
			super.setText(getDefaultText());
			setChangedState(false);
		}
		
	}
	
	/**
	 * Returns the default text for this Field.
	 */
	public String getDefaultText(){
		return defaultText;
	}
	
	/**
	 * Sets the default text for this Field.
	 */
	public void setDefaultText(String defaultText){
		
		this.defaultText = defaultText;
		super.setText(defaultText);
		
	}
	
	/**
	 * @return The text inside of this Field if modified, <code>null</code>
	 *         otherwise.
	 * 
	 * @see javax.swing.text.JTextComponent#getText()
	 *      <i>JTextComponent</i>.getText()
	 */
	@Override
	public String getText(){
		
		String text = null;
		
		if(isChanged){
			text = super.getText();
		}
		
		return text;
	}
	
	@Override
	public void setText(String t){
		this.simulateInput(t);
	}
	
	private void simulateInput(String input){
		
		this.setChangedState(true);
		this.setVisualState(true);
		super.setText(input);
		
	}
	
	/**
	 * Returns the state of whether this Field has been modified or not.
	 */
	public boolean isChanged(){
		return isChanged;
	}
	
	/**
	 * Change the state of this Field to know whether this Field's value's the
	 * default one or not.
	 */
	public void setChangedState(boolean isChanged){
		this.isChanged = isChanged;
	}
	
	@Override
	public void focusGained(FocusEvent e){
		
		if(isChanged() == false){
			setVisualState(true);
		}
		
	}
	
	@Override
	public void focusLost(FocusEvent e){
		
		if(super.getText().matches("\\s*")){
			setVisualState(false);
		}
		else{
			setChangedState(true);
		}
		
	}
	
	/**
	 * @return A JScrollBar containing this Field.
	 */
	public JScrollPane getAsScrollPane(){
		return new JScrollPane(this);
	}
	
}
