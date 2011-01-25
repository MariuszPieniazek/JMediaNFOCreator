package pl.JMediaNFOCreator;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;

public class ReadingTagsProgressBarDialog extends Dialog {

	protected Object result;
	protected Shell shellReadingTagsProgressBarDialog;
	private ProgressBar progressBarTags;
	private ProgressBar progressBarFiles;
	private Label labelProgressBarTags, labelProgressBarFiles;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public ReadingTagsProgressBarDialog(Shell parent, int style) {
		super(parent, style);
		setText("SWT Dialog");
	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public Object open() {
		createContents();
		shellReadingTagsProgressBarDialog.open();
		shellReadingTagsProgressBarDialog.layout();
		Shell shellParent = getParent( );
		Rectangle shellParentBounds = shellParent.getBounds();
        Point shellDialogSize = shellReadingTagsProgressBarDialog.getSize();
        shellReadingTagsProgressBarDialog.setLocation(shellParentBounds.x + (shellParentBounds.width - shellDialogSize.x) / 2, shellParentBounds.y + (shellParentBounds.height - shellDialogSize.y) / 2);
		return result;
	}

	/**
	 * Create contents of the dialog.
	 */
	private void createContents() {
		shellReadingTagsProgressBarDialog = new Shell(getParent(), getStyle());
		shellReadingTagsProgressBarDialog.setSize(455, 178);
		shellReadingTagsProgressBarDialog.setText(getText());
		
		Composite compositeReadingTagsProgressBarDialog = new Composite(shellReadingTagsProgressBarDialog, SWT.NONE);
		compositeReadingTagsProgressBarDialog.setBounds(10, 10, 424, 138);
		
		progressBarTags = new ProgressBar(compositeReadingTagsProgressBarDialog, SWT.SMOOTH);
		progressBarTags.setBounds(25, 31, 378, 17);
		
		progressBarFiles = new ProgressBar(compositeReadingTagsProgressBarDialog, SWT.SMOOTH);
		progressBarFiles.setBounds(25, 84, 378, 17);
		
		Button btnCancel = new Button(compositeReadingTagsProgressBarDialog, SWT.PUSH );
		btnCancel.setText("Anuluj");
		
		labelProgressBarTags = new Label(compositeReadingTagsProgressBarDialog, SWT.NONE);
		labelProgressBarTags.setBounds(25, 10, 378, 15);
		
		labelProgressBarFiles = new Label(compositeReadingTagsProgressBarDialog, SWT.NONE);
		labelProgressBarFiles.setBounds(25, 63, 378, 15);

	}
	
	public void setLabelProgressBarTagsTxt(String labelProgressBarTagsTxt) {
		labelProgressBarTags.setText(labelProgressBarTagsTxt);
	}
	
	public void setLabelProgressBarFilesTxt(String labelProgressBarFilesTxt) {
		labelProgressBarFiles.setText(labelProgressBarFilesTxt);
	}
	
	public Object close() {
		shellReadingTagsProgressBarDialog.dispose();
		return result;
	}
	
	public ProgressBar getProgressBarTags() {
		return progressBarTags;
	}
	
	public void setProgressBarFilesMinimum(int progressBarFilesMinimum) {
		progressBarFiles.setMinimum(progressBarFilesMinimum);
	}
	
	public void setProgressBarFilesMaximum(int progressBarFilesMaximum) {
		progressBarFiles.setMaximum(progressBarFilesMaximum);
	}
	
	public void setProgressBarFilesSelection(int progressBarFilesSelection) {
		progressBarFiles.setSelection(progressBarFilesSelection);
	}
	
	public void setProgressBarTagsMinimum(int progressBarTagsMinimum) {
		progressBarTags.setMinimum(progressBarTagsMinimum);
	}
	
	public void setProgressBarTagsMaximum(int progressBarTagsMaximum) {
		progressBarTags.setMaximum(progressBarTagsMaximum);
	}
	
	public void setProgressBarTagsSelection(int progressBarTagsSelection) {
		progressBarTags.setSelection(progressBarTagsSelection);
	}
	
	public ProgressBar getProgressBarFils() {
		return progressBarFiles;
	}
}
