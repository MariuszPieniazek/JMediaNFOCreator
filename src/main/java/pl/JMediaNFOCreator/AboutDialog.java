package pl.JMediaNFOCreator;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

public class AboutDialog extends Dialog {
    AboutDialog(Shell parent)
    {
        super(parent);        
    }
    
    public void open( )
    {
        Shell shellParent = getParent(); 
        final Shell shellAboutDialog = new Shell(shellParent, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
        shellAboutDialog.setSize(200,100);
        Rectangle shellParentBounds = shellParent.getBounds();
        Point shellDialogSize = shellAboutDialog.getSize();
        shellAboutDialog.setLocation(shellParentBounds.x + (shellParentBounds.width - shellDialogSize.x) / 2, shellParentBounds.y + (shellParentBounds.height - shellDialogSize.y) / 2); 
        shellAboutDialog.setText(Messages.AD_shellAboutDialogTxt); 
        final Label l = new Label(shellAboutDialog, SWT.NONE);
        l.setText(Messages.AD_lTxt);
        l.setBounds(43, 20, 110, 20);
        Button b = new Button(shellAboutDialog, SWT.PUSH);
        b.setText(Messages.AD_bTxt);
        b.setBounds(80, 45, 40, 25);
        b.addSelectionListener(new SelectionAdapter( ) {
            public void widgetSelected(SelectionEvent e) {
            	shellAboutDialog.dispose( );
            }
        }); 
        shellAboutDialog.open( ); 
        Display display = shellParent.getDisplay( ); 
        while (!shellAboutDialog.isDisposed( )) 
        { if (!display.readAndDispatch( )) display.sleep( ); 
        } 
    }
}
