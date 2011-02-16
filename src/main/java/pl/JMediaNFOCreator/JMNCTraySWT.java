package pl.JMediaNFOCreator;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tray;
import org.eclipse.swt.widgets.TrayItem;

public class JMNCTraySWT {
	
	private Tray tray;
	private TrayItem trayItem;
	
	public JMNCTraySWT(Shell parent) {
		final Shell shellParent = parent;
		Display display = shellParent.getDisplay();
		tray = display.getSystemTray();
		if (tray == null) {
			System.out.println ("The system tray is not available");
		} else {
			trayItem = new TrayItem (tray, SWT.NONE);
			trayItem.setImage(new Image(display, this.getClass().getResourceAsStream("/" + "Crystal_Clear_app_xmms.png")));
			trayItem.setToolTipText("JMediaNFOCreator");
			
			trayItem.addSelectionListener(new SelectionAdapter() {
				public void widgetDefaultSelected(SelectionEvent e) {			
				shellParent.setMinimized(false);
				shellParent.setFocus();
				shellParent.open();
				trayItem.dispose();
				}
				});
			
			trayItem.addListener(SWT.Show, new Listener () {
				public void handleEvent(Event event) {
					shellParent.setMinimized(false);
				}
			});
			trayItem.addListener(SWT.Iconify, new Listener () {
				public void handleEvent(Event event) {
					shellParent.setMinimized(true);
				}
			});
			trayItem.addListener(SWT.Hide, new Listener () {
				public void handleEvent (Event event) {
					shellParent.setVisible(false);
				}
			});
			trayItem.addListener(SWT.Selection, new Listener () {
				public void handleEvent(Event event) {
				}
			});
			trayItem.addListener(SWT.DefaultSelection, new Listener () {
				public void handleEvent(Event event) {
				}
			});
			final Menu menu = new Menu(shellParent, SWT.POP_UP);
			for (int i = 0; i < 8; i++) {
				MenuItem mi = new MenuItem (menu, SWT.PUSH);
				mi.setText ("Item" + i);
				mi.addListener (SWT.Selection, new Listener () {
					public void handleEvent (Event event) {
						System.out.println("selection " + event.widget);
					}
				});
				if (i == 0) menu.setDefaultItem(mi);
			}
			trayItem.addListener (SWT.MenuDetect, new Listener () {
				public void handleEvent (Event event) {
					menu.setVisible (true);
				}
			});
		}
	}
	
	public void dispose() {
		 if (trayItem != null && !trayItem.isDisposed()) {
			 trayItem.dispose();
		 }				
	}		 
}