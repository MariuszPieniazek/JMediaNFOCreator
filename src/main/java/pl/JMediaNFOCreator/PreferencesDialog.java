package pl.JMediaNFOCreator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;

public class PreferencesDialog extends Dialog {

	PreferencesDialog(Shell parent) {
		super(parent);
	}

	private Object result;
	private Shell shellPreferencesDialog;
	private CTabFolder cTabFolderPreferences;
	private Button btnPreferencesCancel;
	private Button btnGenerateSumOfControl;
	private Button btnAddBitrate;
	private Button btnWriteUnicodeFilenames;
	private List listLanguages;
	private Label lblDisplayChoosenLangauage;
	private Label lblChoosenLangauage;
	private Text txtDefaultDir;
	private Combo comboGenerateSumOfControl;
	private TableColumn tblClmnChecksum;

	/**
	 * Create the dialog.
	 * 
	 * @param parent
	 * @param style
	 * @wbp.parser.constructor
	 */
	public PreferencesDialog(Shell parent, int style,
			TableColumn aTblClmnChecksum) {
		super(parent, style);
		setText(Messages.PD_this_text);
		tblClmnChecksum = aTblClmnChecksum;
	}

	/**
	 * Open the dialog.
	 * 
	 * @return the result
	 */
	public Object open() {
		createContentsPreferencesDialog(shellPreferencesDialog);
		shellPreferencesDialog.setSize(500, 320);

		Shell shellParent = getParent();
		Display display = shellParent.getDisplay();

		shellPreferencesDialog.layout();
		Rectangle shellParentBounds = shellParent.getBounds();

		Point shellDialogSize = shellPreferencesDialog.getSize();
		shellPreferencesDialog.setLocation(shellParentBounds.x
				+ (shellParentBounds.width - shellDialogSize.x) / 2,
				shellParentBounds.y
						+ (shellParentBounds.height - shellDialogSize.y) / 2);

		shellPreferencesDialog.open();
		while (!shellPreferencesDialog.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}

		return result;
	}

	/**
	 * Create contents of the dialog.
	 */
	private void createContentsPreferencesDialog(Composite parent) {
		shellPreferencesDialog = new Shell(getParent(), getStyle());
		shellPreferencesDialog.setText(getText());
		shellPreferencesDialog.setLayout(new FormLayout());

		cTabFolderPreferences = new CTabFolder(shellPreferencesDialog,
				SWT.BORDER);
		cTabFolderPreferences.setSimple(false);
		{
			FormLayout layout = new FormLayout();
			cTabFolderPreferences.setLayout(layout);
			FormData data = new FormData();
			data.left = new FormAttachment(0);
			data.right = new FormAttachment(100);
			data.top = new FormAttachment(0);
			data.bottom = new FormAttachment(87);
			cTabFolderPreferences.setLayoutData(data);
		}

		Composite compositePreferencesDialog = new Composite(
				shellPreferencesDialog, SWT.NONE);
		{
			FormLayout layout = new FormLayout();
			layout.marginTop = 5;
			layout.marginLeft = 5;
			layout.marginRight = 5;
			layout.spacing = 5;
			compositePreferencesDialog.setLayout(layout);
			FormData data = new FormData();
			data.left = new FormAttachment(0);
			data.right = new FormAttachment(100);
			data.top = new FormAttachment(0);
			data.bottom = new FormAttachment(100);
			compositePreferencesDialog.setLayoutData(data);
		}

		generalCTab();
		languageCTab();

		Button btnPreferencesOK = new Button(compositePreferencesDialog,
				SWT.PUSH);
		btnPreferencesOK.setText(Messages.PD_btnPreferencesOK_Txt);
		{
			FormData data = new FormData();
			data.right = new FormAttachment(btnPreferencesCancel, 420);
			data.bottom = new FormAttachment(100, -5);
			btnPreferencesOK.setLayoutData(data);
			btnPreferencesOK.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(final SelectionEvent event) {
					handleBtnPreferencesOKSelected(event);
				}
			});
		}

		btnPreferencesCancel = new Button(compositePreferencesDialog, SWT.PUSH);
		btnPreferencesCancel.setText(Messages.PD_btnPreferencesCancel_Txt);
		{
			FormData data = new FormData();
			data.right = new FormAttachment(100, -5);
			data.bottom = new FormAttachment(100, -5);
			btnPreferencesCancel.setLayoutData(data);
			btnPreferencesCancel.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(final SelectionEvent event) {
					handleBtnPreferencesCancelSelected(event);
				}
			});
		}
	}

	protected void handleBtnPreferencesCancelSelected(final SelectionEvent e) {
		shellPreferencesDialog.dispose();
	}

	protected void handleBtnPreferencesOKSelected(final SelectionEvent e) {
		try {
			String userHome = System.getProperty("user.home");
			if (userHome == null) {
				throw new IllegalStateException("user.home==null");
			}
			File settingsDirectory = new File(new File(userHome),
					"." + JMediaNFOCreator.PROGRAM_NAME);
			if (!settingsDirectory.exists()) {
				settingsDirectory.mkdir();
				if (!settingsDirectory.mkdir()) {
					throw new IllegalStateException(
							settingsDirectory.toString() + "Awaria3");
				}
			}

			FileOutputStream outputStream = new FileOutputStream(
					settingsDirectory + File.separator + JMediaNFOCreator.PROGRAM_NAME + ".xml");
			Properties prop = new Properties();
			if (btnGenerateSumOfControl.getSelection() == false) {
				prop.setProperty("autocrc", "0");
			} else
				prop.setProperty("autocrc", "1");
			prop.setProperty("language", lblDisplayChoosenLangauage.getText());
			prop.setProperty("defaultpath", txtDefaultDir.getText());
			if (btnAddBitrate.getSelection() == false) {
				prop.setProperty("addbitrate", "0");
			} else
				prop.setProperty("addbitrate", "1");
			if (btnWriteUnicodeFilenames.getSelection() == false) {
				prop.setProperty("saveunicode", "0");
			} else
				prop.setProperty("saveunicode", "1");
			prop.setProperty("crc", comboGenerateSumOfControl.getText());
			prop.storeToXML(outputStream,
					"Konfiguracja programu " + JMediaNFOCreator.PROGRAM_NAME);
			outputStream.close();

		} catch (IOException e1) {
			e1.printStackTrace();
		}
		if (btnGenerateSumOfControl.getSelection() == false)
			tblClmnChecksum.dispose();
		shellPreferencesDialog.dispose();
	}

	public void generalCTab() {
		CTabItem cTabGeneral = new CTabItem(cTabFolderPreferences, SWT.NONE);
		cTabGeneral.setText(Messages.PD_cTabGeneralTxt);
		cTabGeneral.setToolTipText(Messages.PD_cTabGeneral_toolTipTxt);

		Composite compositeGeneral = new Composite(cTabFolderPreferences,
				SWT.NONE);
		{
			FormLayout layout = new FormLayout();
			layout.marginTop = 5;
			layout.marginLeft = 5;
			layout.marginRight = 5;
			layout.spacing = 5;
			compositeGeneral.setLayout(layout);
			FormData data = new FormData();
			data.left = new FormAttachment(0);
			data.right = new FormAttachment(100);
			data.top = new FormAttachment(0);
			data.bottom = new FormAttachment(100);
			compositeGeneral.setLayoutData(data);
		}

		btnAddBitrate = new Button(compositeGeneral, SWT.CHECK);
		btnAddBitrate.setText(Messages.PD_btnAddBitrateTxt);
		{
			FormData data = new FormData();
			data.left = new FormAttachment(0);
			data.top = new FormAttachment(0, 10);
			btnAddBitrate.setLayoutData(data);
			if (new FilesOperations().getCfgProperty(JMediaNFOCreator.PROGRAM_NAME + ".xml",
					"addbitrate").equals("1")) {
				btnAddBitrate.setSelection(true);
			} else
				btnAddBitrate.setSelection(false);
		}

		btnGenerateSumOfControl = new Button(compositeGeneral, SWT.CHECK);
		btnGenerateSumOfControl.setText(Messages.PD_btnGenearteSumOfControlTxt);
		{
			FormData data = new FormData();
			data.left = new FormAttachment(0);
			data.top = new FormAttachment(btnAddBitrate, 5);
			btnGenerateSumOfControl.setLayoutData(data);
			if (new FilesOperations().getCfgProperty(JMediaNFOCreator.PROGRAM_NAME + ".xml",
					"autocrc").equals("1")) {
				btnGenerateSumOfControl.setSelection(true);
			} else
				btnGenerateSumOfControl.setSelection(false);
		}

		comboGenerateSumOfControl = new Combo(compositeGeneral, SWT.LEFT);
		{
			FormData data = new FormData();
			data.left = new FormAttachment(btnGenerateSumOfControl, 5);
			data.top = new FormAttachment(btnAddBitrate, 5);
			comboGenerateSumOfControl.setLayoutData(data);
			comboGenerateSumOfControl.add("SFV");
			comboGenerateSumOfControl.add("MD5");
			if (new FilesOperations().getCfgProperty(JMediaNFOCreator.PROGRAM_NAME + ".xml",
					"crc").equals("MD5")) {
				comboGenerateSumOfControl.select(1);
			} else
				comboGenerateSumOfControl.select(0);
		}

		Label lblDefaultDir = new Label(compositeGeneral, SWT.LEFT);
		lblDefaultDir.setText(Messages.PD_lblDefaultDirTxt);
		{
			FormData data = new FormData();
			data.left = new FormAttachment(3);
			data.top = new FormAttachment(btnGenerateSumOfControl, 5);
			lblDefaultDir.setLayoutData(data);
		}

		txtDefaultDir = new Text(compositeGeneral, SWT.BORDER);
		{
			FormData data = new FormData();
			data.left = new FormAttachment(lblDefaultDir, 2);
			data.right = new FormAttachment(92);
			data.top = new FormAttachment(btnGenerateSumOfControl, 5);
			txtDefaultDir.setLayoutData(data);
			txtDefaultDir.setText(new FilesOperations().getCfgProperty(
					"JMediaNFOCreator.xml", "defaultpath"));

		}

		Button btnDefaultDir = new Button(compositeGeneral, SWT.PUSH);
		btnDefaultDir.setText(Messages.PD_btnDefaultDir_Txt);
		{
			FormData data = new FormData();
			data.left = new FormAttachment(txtDefaultDir, 5);
			data.top = new FormAttachment(btnGenerateSumOfControl, 5);
			btnDefaultDir.setLayoutData(data);
		}

		btnWriteUnicodeFilenames = new Button(compositeGeneral, SWT.CHECK);
		btnWriteUnicodeFilenames
				.setText(Messages.PD_btnWriteUnicodeFilenames_Txt);
		{
			FormData data = new FormData();
			data.left = new FormAttachment(0);
			data.top = new FormAttachment(lblDefaultDir, 10);
			btnWriteUnicodeFilenames.setLayoutData(data);
			if (new FilesOperations().getCfgProperty(JMediaNFOCreator.PROGRAM_NAME + ".xml",
					"saveunicode").equals("1")) {
				btnWriteUnicodeFilenames.setSelection(true);
			} else
				btnWriteUnicodeFilenames.setSelection(false);
		}

		cTabGeneral.setControl(compositeGeneral);
	}

	public void languageCTab() {
		CTabItem cTabLanguage = new CTabItem(cTabFolderPreferences, SWT.NONE);
		cTabLanguage.setText(Messages.PD_cTabLanguage_Txt);
		cTabLanguage.setToolTipText(Messages.PD_cTabLanguage_toolTipText);

		Composite compositeLanguage = new Composite(cTabFolderPreferences,
				SWT.NONE);
		{
			FormLayout layout = new FormLayout();
			layout.marginTop = 5;
			layout.marginLeft = 5;
			layout.marginRight = 5;
			layout.spacing = 5;
			compositeLanguage.setLayout(layout);
			FormData data = new FormData();
			data.left = new FormAttachment(0);
			data.right = new FormAttachment(100);
			data.top = new FormAttachment(0);
			data.bottom = new FormAttachment(100);
			compositeLanguage.setLayoutData(data);
		}

		Group grpLanguage = new Group(compositeLanguage, SWT.NONE);
		grpLanguage.setText(Messages.PD_grpLanguage_text);
		{
			FormLayout layout = new FormLayout();
			layout.marginTop = 5;
			layout.marginLeft = 5;
			layout.marginRight = 5;
			layout.spacing = 5;
			FormData data = new FormData();
			data.left = new FormAttachment(0);
			data.right = new FormAttachment(100);
			data.top = new FormAttachment(0);
			data.bottom = new FormAttachment(100);
			grpLanguage.setLayoutData(data);
			grpLanguage.setLayout(layout);
		}

		listLanguages = new List(grpLanguage, SWT.LEFT | SWT.V_SCROLL
				| SWT.BORDER);
		{
			FormData data = new FormData();
			data.left = new FormAttachment(0);
			data.right = new FormAttachment(35);
			data.top = new FormAttachment(0);
			data.bottom = new FormAttachment(50);
			listLanguages.setLayoutData(data);

			/*String selectedDirectoryLang = this.getClass().getResource("/lang")
					.getPath();
			if (selectedDirectoryLang != null) {
				String[] filterFilesLang = new DirList().getDirList(".*"
						+ File.separatorChar + ".lng", selectedDirectoryLang);
				if (filterFilesLang.length > 0)
					listLanguages.setItems(filterFilesLang);
			}*/
		}

		Button btnChooseLanguage = new Button(grpLanguage, SWT.PUSH);
		btnChooseLanguage.setText(Messages.PD_btnChooseLanguage_Txt);
		{
			FormData data = new FormData();
			data.left = new FormAttachment(0);
			data.right = new FormAttachment(35);
			data.top = new FormAttachment(listLanguages, 5);
			btnChooseLanguage.setLayoutData(data);
			btnChooseLanguage.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(final SelectionEvent event) {
					handleBtnChooseLanguageSelected(event);
				}
			});
		}

		lblChoosenLangauage = new Label(grpLanguage, SWT.LEFT);
		lblChoosenLangauage.setText(Messages.PD_lblChoosenLangauage_text);
		{
			FormData data = new FormData();
			data.left = new FormAttachment(0);
			data.right = new FormAttachment(35);
			data.top = new FormAttachment(btnChooseLanguage, 5);
			lblChoosenLangauage.setLayoutData(data);
		}

		lblDisplayChoosenLangauage = new Label(grpLanguage, SWT.BORDER);
		{
			FormData data = new FormData();
			data.left = new FormAttachment(0);
			data.right = new FormAttachment(35);
			data.top = new FormAttachment(lblChoosenLangauage, 5);
			lblDisplayChoosenLangauage.setLayoutData(data);
			lblDisplayChoosenLangauage.setText(new FilesOperations()
					.getCfgProperty(JMediaNFOCreator.PROGRAM_NAME + ".xml", "language"));
		}

		Label lblLangauage = new Label(grpLanguage, SWT.RIGHT);
		lblLangauage.setText(Messages.PD_lblLangauage_text);
		{
			FormData data = new FormData();
			data.left = new FormAttachment(50);
			data.top = new FormAttachment(listLanguages, 5, SWT.TOP);
			lblLangauage.setLayoutData(data);
		}

		Label lblDisplayLangauage = new Label(grpLanguage, SWT.LEFT);
		{
			FormData data = new FormData();
			data.left = new FormAttachment(lblLangauage, 5);
			data.top = new FormAttachment(listLanguages, 5, SWT.TOP);
			lblDisplayLangauage.setLayoutData(data);
		}

		Label lblAuthor = new Label(grpLanguage, SWT.RIGHT);
		lblAuthor.setText(Messages.PD_lblAuthor_text);
		{
			FormData data = new FormData();
			data.left = new FormAttachment(50);
			data.top = new FormAttachment(lblLangauage, 5);
			lblAuthor.setLayoutData(data);
		}

		Label lblDisplayAuthor = new Label(grpLanguage, SWT.LEFT);
		{
			FormData data = new FormData();
			data.left = new FormAttachment(lblAuthor, 5);
			data.top = new FormAttachment(lblLangauage, 5);
			lblDisplayAuthor.setLayoutData(data);
		}

		Label lblVersion = new Label(grpLanguage, SWT.RIGHT);
		lblVersion.setText(Messages.PD_lblVersion_text);
		{
			FormData data = new FormData();
			data.left = new FormAttachment(50);
			data.top = new FormAttachment(lblAuthor, 5);
			lblVersion.setLayoutData(data);
		}

		Label lblDisplayVersion = new Label(grpLanguage, SWT.LEFT);
		{
			FormData data = new FormData();
			data.left = new FormAttachment(lblVersion, 5);
			data.top = new FormAttachment(lblAuthor, 5);
			lblDisplayVersion.setLayoutData(data);
		}

		Text txtInfoAboutTranslate = new Text(grpLanguage, SWT.BORDER
				| SWT.WRAP);
		txtInfoAboutTranslate.setText(Messages.PD_txtInfoAboutTranslate_text);
		{
			FormData data = new FormData();
			data.left = new FormAttachment(listLanguages, 5);
			data.right = new FormAttachment(100);
			data.top = new FormAttachment(lblVersion, 5);
			data.bottom = new FormAttachment(lblDisplayChoosenLangauage, 0,
					SWT.BOTTOM);
			txtInfoAboutTranslate.setLayoutData(data);
		}

		cTabLanguage.setControl(compositeLanguage);
	}

	protected void handleBtnChooseLanguageSelected(final SelectionEvent e) {
		lblDisplayChoosenLangauage.setText(listLanguages.getItem(listLanguages
				.getSelectionIndex()));
	}

	public Object close() {
		shellPreferencesDialog.dispose();
		return result;
	}

}
