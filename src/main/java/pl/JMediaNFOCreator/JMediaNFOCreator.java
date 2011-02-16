package pl.JMediaNFOCreator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;
import java.util.Locale;
import java.util.zip.CRC32;

import org.eclipse.nebula.widgets.calendarcombo.CalendarCombo;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.FileTransfer;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.events.ShellListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.jaudiotagger.tag.FieldKey;

public class JMediaNFOCreator {
	final static String PROGRAM_NAME = "JMediaNFOCreator";
	
	private CTabFolder cTabFolder;
	protected Shell shell;
	private Display display;
	private Table tableOfFiles;
	private TableColumn tblClmnChecksum;
	private Button btnNextInfoTab;
	private Button btnFront;
	private Button btnBack;
	private Button btnCD;
	private Button btnVariousArtists;
	private Button btnRemoveGroupsMinus;
	private Button btnAddGroupPlus;
	private Button btnAttachNote;
	private Button btnChecksums;
	private Button btnPlaylistPLS;
	private Button btnPlaylistM3U;
	private Button btnEACLog;
	private Button btnPAR;
	private Button btnPAR2;
	private Button btnCUESheet;
	private Text txtPath;
	private Text txtFiles;
	private Text txtSampleRate;
	private Text txtMpegLayer;
	private Text txtChannels;
	private Text txtEncoder;
	private Text txtAvgBitrate;
	private Text txtYear;
	private Text txtV2TagId;
	private Text txtV1TagId;
	private String selectedDirectory;
	private ArrayList<String> filterFiles;
	private CTabItem cTabPreview;
	private CTabItem cTabItemInformation;
	private Combo comboGenres;
	private Combo comboServerNews;
	private Combo comboNewsGroup;
	private Combo comboSources;
	private Combo comboRipper;
	private Combo comboModelCD_DVD;
	private Text txtSumLenght;
	private Text txtSumOfTimeTracks;
	private Text txtRipper;
	private Text txtPoster;
	private Text txtInfoOfDisc;
	private Text txtAlbum;
	private Text txtPreview;
	private Text txtAttachNote;
	private Text txtQuality;
	private Text txtFilenames;
	private List listGroups;
	private CalendarCombo calComboDateRip;
	private CalendarCombo calComboDatePost;
	private Label lblNFO;
	private Label lblstateNFO;
	private JMNCTraySWT traySWT;
	
	public void createContents() {
		shell = new Shell(display);
		shell.setText(getClass().getSimpleName());
		shell.setSize(800, 600);
		Monitor primary = display.getPrimaryMonitor();
		Rectangle bounds = primary.getBounds();
		Rectangle rect = shell.getBounds();
		int x = bounds.x + (bounds.width - rect.width) / 6;
		int y = bounds.y + (bounds.height - rect.height) / 6;
		shell.setLocation(x, y);
		shell.addShellListener(new ShellListener() {
			
			@Override
			public void shellIconified(ShellEvent e) {
				traySWT = new JMNCTraySWT(shell);
			}

			@Override
			public void shellDeiconified(ShellEvent e) {
				traySWT.dispose();
			}

			@Override
			public void shellDeactivated(ShellEvent e) {
			}

			@Override
			public void shellClosed(ShellEvent e) {
			}

			@Override
			public void shellActivated(ShellEvent e) {
			}
			});

		
		DropTarget target = new DropTarget(shell, DND.DROP_MOVE | DND.DROP_COPY
				| DND.DROP_DEFAULT);
		{
			final TextTransfer textTransfer = TextTransfer.getInstance();
			final FileTransfer fileTransfer = FileTransfer.getInstance();
			Transfer[] types = new Transfer[] { fileTransfer, textTransfer };
			target.setTransfer(types);
			target.addDropListener(new DropTargetAdapter() {
				public void dragEnter(DropTargetEvent event) {
					if (event.detail == DND.DROP_DEFAULT) {
						if ((event.operations & DND.DROP_COPY) != 0) {
							event.detail = DND.DROP_COPY;
						} else {
							event.detail = DND.DROP_NONE;
						}
					}
					for (int i = 0; i < event.dataTypes.length; i++) {
						if (fileTransfer.isSupportedType(event.dataTypes[i])) {
							event.currentDataType = event.dataTypes[i];
							if (event.detail != DND.DROP_COPY) {
								event.detail = DND.DROP_NONE;
							}
							break;
						}
					}
				}

				public void dragOver(DropTargetEvent event) {
					event.feedback = DND.FEEDBACK_SELECT | DND.FEEDBACK_SCROLL;
					if (textTransfer.isSupportedType(event.currentDataType)) {
						Object o = textTransfer
								.nativeToJava(event.currentDataType);
						String t = (String) o;
						if (t != null)
							System.out.println(t);
					}
				}

				public void dragOperationChanged(DropTargetEvent event) {
					if (event.detail == DND.DROP_DEFAULT) {
						if ((event.operations & DND.DROP_COPY) != 0) {
							event.detail = DND.DROP_COPY;
						} else {
							event.detail = DND.DROP_NONE;
						}
					}
					if (fileTransfer.isSupportedType(event.currentDataType)) {
						if (event.detail != DND.DROP_COPY) {
							event.detail = DND.DROP_NONE;
						}
					}
				}

				public void drop(DropTargetEvent event) {
					if (textTransfer.isSupportedType(event.currentDataType)) {
						String path = (String) event.data;
						System.out.println(path);
						if (path != null) {
							txtPath.setText(path);
							DirList listFiles = new DirList();
							String[] fileTypes = {".*" + File.separatorChar + ".mp3", ".*" + File.separatorChar + ".ogg"};
							filterFiles = listFiles.getDirList(fileTypes, path);
							if (filterFiles.size() == 0) {
								txtFiles.setText("Brak plików mp3 w wybranym katalogu.");
								btnNextInfoTab.setEnabled(false);
							} else {
								txtFiles.setText("");
								btnNextInfoTab.setEnabled(false);
							}
							for (String file : filterFiles) {
								txtFiles.append(path + File.separatorChar
										+ file + "\n");
							}
							btnNextInfoTab.setEnabled(true);
						}
					}
					if (fileTransfer.isSupportedType(event.currentDataType)) {
						String[] path = (String[]) event.data;
						txtPath.insert(path[0]);
						if (path[0] != null) {
							txtPath.setText(path[0]);
							DirList listFiles = new DirList();
							String[] fileTypes = {".*" + File.separatorChar + ".mp3", ".*" + File.separatorChar + ".ogg"};
							filterFiles = listFiles.getDirList(fileTypes, path[0]);
							if (filterFiles.size() == 0) {
								txtFiles.setText("Brak plików mp3 w wybranym katalogu.");
								btnNextInfoTab.setEnabled(false);
							} else {
								txtFiles.setText("");
								btnNextInfoTab.setEnabled(false);
							}
							for (String file : filterFiles) {
								txtFiles.append(path[0] + File.separatorChar
										+ file + "\n");
							}
							btnNextInfoTab.setEnabled(true);
						}
					}
				}
			});
		}

		FormLayout layout = new FormLayout();
		{
			layout.marginTop = 5;
			layout.marginLeft = 5;
			layout.marginRight = 5;
			layout.spacing = 5;
			shell.setLayout(layout);
		}

		cTabFolder = new CTabFolder(shell, SWT.NONE);
		{
			FormData formData = new FormData();
			formData.right = new FormAttachment(100);
			formData.left = new FormAttachment(0);
			formData.top = new FormAttachment(0);
			formData.bottom = new FormAttachment(100, -20);
			cTabFolder.setLayoutData(formData);
			cTabFolder.setSimple(false);
		}

		lblstateNFO = new Label(shell, SWT.NONE);
		{
			FormData formData = new FormData();
			formData.bottom = new FormAttachment(100);
			lblstateNFO.setLayoutData(formData);
		}
		lblstateNFO.setText(Messages.JMNB_LblStateNFOText);

		choiceCTab();
		informationCTab();
		addonsCTab();
		browserCTab();
		menu();
		shell.pack();
	}

	public void setLblStateNFO(String tekst) {
		lblstateNFO.setText(tekst);
	}

	public void menu() {
		Menu bar = new Menu(shell, SWT.BAR);
		shell.setMenuBar(bar);
		MenuItem fileItem1 = new MenuItem(bar, SWT.CASCADE);
		fileItem1.setText(Messages.JMNB_MenuItemFileText);
		Menu submenu1 = new Menu(shell, SWT.DROP_DOWN);
		fileItem1.setMenu(submenu1);
		MenuItem item11 = new MenuItem(submenu1, SWT.PUSH);
		item11.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				System.out.println("Save");
			}
		});
		item11.setText(Messages.JMNB_MenuItemSaveText);
		item11.setAccelerator(SWT.MOD1 + 'S');

		MenuItem item12 = new MenuItem(submenu1, SWT.PUSH);
		item12.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent arg0) {
				System.exit(0);
			}
		});
		item12.setText(Messages.JMNB_MenuItemExitText);
		item12.setAccelerator(SWT.MOD1 + 'X');

		MenuItem fileItem2 = new MenuItem(bar, SWT.CASCADE);
		fileItem2.setText(Messages.JMNB_MenuItemToolsText);
		Menu submenu2 = new Menu(shell, SWT.DROP_DOWN);
		fileItem2.setMenu(submenu2);

		MenuItem itemSettings = new MenuItem(submenu2, SWT.PUSH);
		itemSettings.setText(Messages.JMNB_MenuItemPreferencesText);
		itemSettings.setAccelerator(SWT.MOD1 + 'P');
		itemSettings.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent event) {
				handleItemSettingsSelected(event);
			}
		});

		MenuItem fileItem3 = new MenuItem(bar, SWT.CASCADE);
		fileItem3.setText(Messages.JMNB_MenuItemHelpText);
		Menu submenu3 = new Menu(shell, SWT.DROP_DOWN);
		fileItem3.setMenu(submenu3);

		MenuItem aboutMenuItem = new MenuItem(submenu3, SWT.PUSH);
		aboutMenuItem.setText(Messages.JMNB_AboutMenuItemText);
		aboutMenuItem.setAccelerator(SWT.MOD1 + 'A');
		aboutMenuItem.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				new AboutDialog(shell).open();
			}
		});
	}

	protected void handleItemSettingsSelected(final SelectionEvent e) {
		PreferencesDialog dialogPreferences = new PreferencesDialog(shell,
				SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL, tblClmnChecksum);
		dialogPreferences.open();
	}

	protected void choiceCTab() {
		CTabItem cTabItemChoice = new CTabItem(cTabFolder, SWT.NONE);
		cTabItemChoice.setText(Messages.JMNB_tab_text); // Tekst na zakładce
		cTabItemChoice.setToolTipText(Messages.JMNB_tab_toolTipText);

		Composite composite = new Composite(cTabFolder, SWT.NONE);
		{
			FormLayout layout = new FormLayout();
			layout.marginTop = 5;
			layout.marginLeft = 5;
			layout.marginRight = 5;
			layout.spacing = 5;
			composite.setLayout(layout);
			FormData data = new FormData();
			data.right = new FormAttachment(0);
			data.left = new FormAttachment(100);
			composite.setLayoutData(data);
		}

		Group group1 = new Group(composite, SWT.NONE);
		group1.setText(Messages.JMNB_group1_text);
		{
			FormLayout layout = new FormLayout();
			layout.marginTop = 5;
			layout.marginLeft = 5;
			layout.marginRight = 5;
			layout.spacing = 5;
			FormData data = new FormData();
			data.right = new FormAttachment(100, -5);
			data.left = new FormAttachment(0);
			group1.setLayoutData(data);
			group1.setLayout(layout);
		}

		txtPath = new Text(group1, SWT.BORDER | SWT.SINGLE);
		{
			FormData data = new FormData();
			data.right = new FormAttachment(100, -25);
			data.left = new FormAttachment(0);
			txtPath.setLayoutData(data);
			data = new FormData();
			data.right = new FormAttachment(100);
			data.left = new FormAttachment(0);
			txtPath.setEditable(false);
			txtPath.setBackground(txtPath.getDisplay().getSystemColor(
					SWT.COLOR_WHITE));
		}

		Button btnChoosePath = new Button(group1, SWT.PUSH);
		btnChoosePath.setText(Messages.JMNB_BtnChoosePathText);
		{
			FormData data = new FormData();
			data.left = new FormAttachment(txtPath);
			btnChoosePath.setLayoutData(data);
		}

		btnChoosePath.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent event) {
				handleBtnChoosePathSelected(event);
			}
		});

		Group group2 = new Group(composite, SWT.NONE);
		group2.setText(Messages.JMNB_group2_text);
		{
			FormLayout layout = new FormLayout();
			layout.marginTop = 5;
			layout.marginLeft = 5;
			layout.marginRight = 5;
			layout.spacing = 5;
			FormData data = new FormData();
			data.right = new FormAttachment(100, -5);
			data.left = new FormAttachment(0);
			data.top = new FormAttachment(group1, 5);
			data.bottom = new FormAttachment(100, -40);
			group2.setLayoutData(data);
			group2.setLayout(layout);
		}

		txtFiles = new Text(group2, SWT.MULTI | SWT.V_SCROLL);
		txtFiles.setEditable(false);
		{
			FormData data = new FormData();
			data.right = new FormAttachment(100);
			data.left = new FormAttachment(0);
			data.top = new FormAttachment(group1, 5);
			data.bottom = new FormAttachment(100, -5);
			txtFiles.setLayoutData(data);
			txtFiles.setBackground(txtFiles.getDisplay().getSystemColor(SWT.COLOR_WHITE));
		}

		btnNextInfoTab = new Button(composite, SWT.PUSH);
		btnNextInfoTab.setEnabled(false);
		{
			FormData data = new FormData();
			data.left = new FormAttachment(85);
			data.right = new FormAttachment(100, -5);
			data.bottom = new FormAttachment(90);
			data.bottom = new FormAttachment(99);
			btnNextInfoTab.setLayoutData(data);
			btnNextInfoTab.setText(Messages.JMNB_BtnNextInfoTabText);

			btnNextInfoTab.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(final SelectionEvent event) {
					handleBtnNextInfoTabSelected(event);
				}
			});
		}

		cTabItemChoice.setControl(composite);
	}

	protected void handleBtnChoosePathSelected(final SelectionEvent e) {
		DirectoryDialog directoryDialog = new DirectoryDialog(shell, SWT.APPLICATION_MODAL | SWT.DIALOG_TRIM);
		directoryDialog.setText("Przeglądanie w poszukiwaniu folderu");
		directoryDialog.setMessage("Wybierz katalog");
		selectedDirectory = directoryDialog.open();
		if (selectedDirectory != null) {
			txtPath.setText(selectedDirectory);
			String[] fileTypes = {(".*" + File.separatorChar + ".mp3"), (".*" + File.separatorChar + ".ogg")};
			filterFiles = new DirList().getDirList(fileTypes, selectedDirectory);
			if (filterFiles.size() == 0) {
				txtFiles.setText("Brak plików mp3/ogg w wybranym katalogu.");
				btnNextInfoTab.setEnabled(false);
			} else {
				txtFiles.setText("");
				btnNextInfoTab.setEnabled(false);
			}
			for (String file : filterFiles) {
				txtFiles.append(selectedDirectory + File.separatorChar + file
						+ "\n");
			}
			btnNextInfoTab.setEnabled(true);
		}
	}

	protected void disposeTblClmnChecksum() {
		tblClmnChecksum.dispose();
	}

	protected void createTblClmnChecksum() {
		if (new FilesOperations().getCfgProperty(JMediaNFOCreator.PROGRAM_NAME + ".xml", "crc")
				.equals("MD5")
				& new FilesOperations().getCfgProperty(JMediaNFOCreator.PROGRAM_NAME + ".xml",
						"autocrc").equals("1")) {
			tblClmnChecksum = new TableColumn(tableOfFiles, SWT.NONE);
			tblClmnChecksum.setWidth(100);
			tblClmnChecksum.setText("MD5");
		} else if (new FilesOperations().getCfgProperty(JMediaNFOCreator.PROGRAM_NAME + ".xml",
				"crc").equals("SFV")
				& new FilesOperations().getCfgProperty(JMediaNFOCreator.PROGRAM_NAME + ".xml",
						"autocrc").equals("1")) {
			tblClmnChecksum = new TableColumn(tableOfFiles, SWT.NONE);
			tblClmnChecksum.setWidth(100);
			tblClmnChecksum.setText("SFV");
		}
	}

	protected void handleBtnNextInfoTabSelected(final SelectionEvent e) {
		ReadingTagsProgressBarDialog readingTagsProgressBarDialog = new ReadingTagsProgressBarDialog(btnNextInfoTab.getShell(), SWT.APPLICATION_MODAL);
		readingTagsProgressBarDialog.open();
		readingTagsProgressBarDialog.setProgressBarTagsMinimum(0);
		readingTagsProgressBarDialog.setProgressBarTagsMaximum(8);
		String checksum = null;
		Long sumBitrate = Long.parseLong("0");
		Float sumSizeOfFile = 0F;
		int lengthInSecs = 0;
		Double sumLengthInSecs = 0.0;
		tableOfFiles.removeAll();
		readingTagsProgressBarDialog.setProgressBarFilesMinimum(0);
		readingTagsProgressBarDialog.setProgressBarFilesMaximum(filterFiles.size());
		if ((new FilesOperations().getCfgProperty(JMediaNFOCreator.PROGRAM_NAME + ".xml",
				"autocrc").equals("0") == true)
				& (tblClmnChecksum instanceof TableColumn == true)
				&& (new FilesOperations().getCfgProperty(
						JMediaNFOCreator.PROGRAM_NAME + ".xml", "crc").equals(tblClmnChecksum
						.getText())) == false) {
			disposeTblClmnChecksum();
		} else if ((tblClmnChecksum instanceof TableColumn == true)
				&& (tblClmnChecksum.isDisposed() == false)) {
			disposeTblClmnChecksum();
			createTblClmnChecksum();
		} else {
			createTblClmnChecksum();
		}

		for (int i = 0; i < filterFiles.size(); i++) {
			TagsReader tagReader = new TagsReader();
			TableItem item = new TableItem(tableOfFiles, SWT.NONE);
			String fileName = filterFiles.get(i);
			readingTagsProgressBarDialog.setLabelProgressBarFilesTxt("Plik: "
					+ fileName);
			readingTagsProgressBarDialog.setProgressBarFilesSelection(i + 1);
			String artist = tagReader.getTagReader(selectedDirectory
					+ File.separatorChar + filterFiles.get(i), FieldKey.ARTIST);
			readingTagsProgressBarDialog.setLabelProgressBarTagsTxt("Autor: "
					+ artist);
			readingTagsProgressBarDialog.setProgressBarTagsSelection(1);
			String title = tagReader.getTagReader(selectedDirectory
					+ File.separatorChar + filterFiles.get(i), FieldKey.TITLE);
			readingTagsProgressBarDialog.setLabelProgressBarTagsTxt("Tytuł: "
					+ title);
			readingTagsProgressBarDialog.setProgressBarTagsSelection(2);
			String album = tagReader.getTagReader(selectedDirectory
					+ File.separatorChar + filterFiles.get(i), FieldKey.ALBUM);
			readingTagsProgressBarDialog.setLabelProgressBarTagsTxt("Album: "
					+ album);
			readingTagsProgressBarDialog.setProgressBarTagsSelection(3);

			DirList fileSize = new DirList();
			// String time =
			// tagReader.getTrackLengthAsString(selectedDirectory+File.separatorChar+filterFiles[i]);
			String bitRate = tagReader.getBitRate(selectedDirectory
					+ File.separatorChar + filterFiles.get(i));
			readingTagsProgressBarDialog.setLabelProgressBarTagsTxt("Bitrate: "
					+ bitRate);
			readingTagsProgressBarDialog.setProgressBarTagsSelection(4);
			if (new FilesOperations().getCfgProperty(JMediaNFOCreator.PROGRAM_NAME + ".xml",
					"crc").equals("MD5") == true
					& new FilesOperations().getCfgProperty(
							JMediaNFOCreator.PROGRAM_NAME + ".xml", "autocrc").equals("1") == true) {
				checksum = MD5Checksum.getMD5Checksum(selectedDirectory
						+ File.separatorChar + filterFiles.get(i));
				readingTagsProgressBarDialog.setLabelProgressBarTagsTxt("MD5: "
						+ checksum);
			} else if (new FilesOperations().getCfgProperty(
					JMediaNFOCreator.PROGRAM_NAME + ".xml", "crc").equals("SFV") == true
					& new FilesOperations().getCfgProperty(
							JMediaNFOCreator.PROGRAM_NAME + ".xml", "autocrc").equals("1") == true) {
				checksum = ComputeCRC32
						.getChecksumValue(new CRC32(), selectedDirectory
								+ File.separatorChar + filterFiles.get(i));
				readingTagsProgressBarDialog.setLabelProgressBarTagsTxt("SFV: "
						+ checksum);
			}

			readingTagsProgressBarDialog.setProgressBarTagsSelection(5);
			sumBitrate += tagReader.getBitrateAsNumer(selectedDirectory
					+ File.separatorChar + filterFiles.get(i));

			Float sizeOfFile = ((fileSize.getFileSize(selectedDirectory
					+ File.separatorChar + filterFiles.get(i))) / 1024F) / 1024;
			Formatter formatter = new Formatter();
			formatter.format("%.2f MB", sizeOfFile);

			sumSizeOfFile += sizeOfFile;

			lengthInSecs = tagReader.getTrackLength(selectedDirectory
					+ File.separatorChar + filterFiles.get(i));
			readingTagsProgressBarDialog.setProgressBarTagsSelection(i + 1);
			sumLengthInSecs += lengthInSecs;
			SimpleDateFormat timeInFormat = new SimpleDateFormat("ss");
			SimpleDateFormat timeOutFormat = new SimpleDateFormat("mm:ss");
			SimpleDateFormat timeOutOverAnHourFormat = new SimpleDateFormat(
					"kk:mm:ss");
			String lenght = null;

			try {
				Date timeIn = timeInFormat.parse(String.valueOf(lengthInSecs));
				final int NO_SECONDS_IN_HOUR = 3600;
				if (lengthInSecs < NO_SECONDS_IN_HOUR) {
					timeOutFormat.format(timeIn);
					lenght = timeOutFormat.format(timeIn);
				} else {
					timeOutOverAnHourFormat.format(timeIn);
					lenght = timeOutOverAnHourFormat.format(timeIn);
				}
			} catch (ParseException e1) {
				e1.printStackTrace();
			}

			if (new FilesOperations().getCfgProperty(JMediaNFOCreator.PROGRAM_NAME + ".xml",
					"autocrc").equals("1")) {
				item.setText(new String[] { fileName, artist, title, album,
						formatter.toString(), lenght, bitRate,
						checksum.toUpperCase() });
			} else {
				item.setText(new String[] { fileName, artist, title, album,
						formatter.toString(), lenght, bitRate });
			}

			txtFilenames.setText("00. " + tableOfFiles.getItem(0).getText(1)
					+ " - " + tableOfFiles.getItem(0).getText(3));
			formatter.flush();
			formatter.close();
		}
		TagsReader tagReader = new TagsReader();
		txtSampleRate.setText(tagReader.getSampleRate(selectedDirectory
				+ File.separatorChar + filterFiles.get(0))
				+ " Hz");
		txtMpegLayer.setText(tagReader.getFormat(selectedDirectory
				+ File.separatorChar + filterFiles.get(0)));
		txtChannels.setText(tagReader.getChannels(selectedDirectory
				+ File.separatorChar + filterFiles.get(0)));
		txtEncoder.setText(tagReader.getEncoder(selectedDirectory + File.separatorChar + filterFiles.get(0)));
		txtAvgBitrate.setText(Long.toString(sumBitrate
				/ (filterFiles.size())));
		txtYear.setText(tagReader.getTagReader(selectedDirectory
				+ File.separatorChar + filterFiles.get(0), FieldKey.YEAR));
		txtV1TagId.setText(tagReader.getV1TagId(selectedDirectory + File.separatorChar + filterFiles.get(0)));
		txtV2TagId.setText(tagReader.getV2TagId(selectedDirectory + File.separatorChar + filterFiles.get(0)));

		Formatter formatter = new Formatter();
		txtSumLenght.setText(formatter.format("%.2f MB", sumSizeOfFile)
				.toString());
		formatter.flush();
		formatter.close();

		SimpleDateFormat timeInFormat = new SimpleDateFormat("ss");
		SimpleDateFormat timeOutFormat = new SimpleDateFormat("mm:ss");
		SimpleDateFormat timeOutOverAnHourFormat = new SimpleDateFormat(
				"kk:mm:ss");
		try {
			Date timeIn = timeInFormat.parse(String.valueOf(sumLengthInSecs));
			final int NO_SECONDS_IN_HOUR = 3600;
			if (sumLengthInSecs < NO_SECONDS_IN_HOUR) {
				timeOutFormat.format(timeIn);
				txtSumOfTimeTracks.setText(timeOutFormat.format(timeIn));
			} else {
				timeOutOverAnHourFormat.format(timeIn);
				txtSumOfTimeTracks.setText(timeOutOverAnHourFormat
						.format(timeIn));
			}
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		readingTagsProgressBarDialog.close();
		cTabFolder.setSelection(cTabItemInformation);
		for (int iterator = 0; iterator < tableOfFiles.getColumnCount(); iterator++) {
			tableOfFiles.getColumn(iterator).pack();
		}
		if (cTabPreview instanceof CTabItem) cTabPreview.dispose();
		previewTab();
		createpreview();
	}

	public void createpreview() {
		txtPreview
				.append("---------------------------------------------------------------------\n");
		txtPreview.append("                      VA - Increase the Dosage\n");
		txtPreview
				.append("---------------------------------------------------------------------\n");
		txtPreview.append("\n");
		if (btnVariousArtists.isEnabled() == true)
			txtPreview.append("Artist...............: Various Artists\n");
		txtPreview
				.append("Album................: " + txtAlbum.getText() + "\n");
		txtPreview.append("Genre................: " + comboGenres.getText()
				+ "\n");
		txtPreview.append("Source...............: " + comboSources.getText()
				+ "\n");
		txtPreview.append("Year.................: " + txtYear.getText() + "\n");
		txtPreview.append("Ripper...............: " + comboRipper.getText()
				+ " & " + comboModelCD_DVD.getText() + "\n");
		txtPreview.append("Codec................: " + txtEncoder.getText()
				+ "\n");
		txtPreview.append("Version..............: " + txtMpegLayer.getText()
				+ "\n");
		txtPreview.append("Quality..............: " + txtQuality.getText()
				+ ", (avg. bitrate: " + txtAvgBitrate.getText() + "kbps)"
				+ "\n");
		txtPreview.append("Channels.............: " + txtChannels.getText()
				+ " / " + txtSampleRate.getText() + "\n");
		txtPreview.append("Tags.................: " + txtV1TagId.getText()
				+ ", " + txtV2TagId.getText() + "\n");
		txtPreview.append("Information..........: " + txtInfoOfDisc.getText()
				+ "\n");
		txtPreview.append("Ripped by............: " + txtRipper.getText()
				+ " on " + calComboDateRip.getDateAsString() + "\n");
		txtPreview.append("Posted by............: " + txtPoster.getText()
				+ " on " + calComboDatePost.getDateAsString() + "\n");
		txtPreview.append("News Server..........: " + comboServerNews.getText()
				+ "\n");
		txtPreview.append("News Group(s)........: " + comboNewsGroup.getText()
				+ "\n");
		txtPreview.append("\n");

		String included = "Included.............: NFO";
		if (btnChecksums.getSelection() == true
				& (new FilesOperations().getCfgProperty(JMediaNFOCreator.PROGRAM_NAME + ".xml",
						"autocrc").equals("1") == true)
				& (new FilesOperations().getCfgProperty(JMediaNFOCreator.PROGRAM_NAME + ".xml",
						"crc").equals("MD5") == true)) {
			included += ", MD5";
		}
		if (btnChecksums.getSelection() == true
				& (new FilesOperations().getCfgProperty(JMediaNFOCreator.PROGRAM_NAME + ".xml",
						"autocrc").equals("1") == true)
				& (new FilesOperations().getCfgProperty(JMediaNFOCreator.PROGRAM_NAME + ".xml",
						"crc").equals("SVF") == true)) {
			included += ", SVF";
		}
		if (btnPlaylistPLS.getSelection() == true) {
			included += ", PLS";
		}
		if (btnPlaylistM3U.getSelection() == true) {
			included += ", M3U";
		}
		if (btnEACLog.getSelection() == true) {
			included += ", LOG";
		}
		if (btnPAR.getSelection() == true) {
			included += ", PAR";
		}
		if (btnPAR2.getSelection() == true) {
			included += ", PAR2";
		}
		if (btnCUESheet.getSelection() == true) {
			included += ", CUE";
		}
		txtPreview.append(included + "\n");

		txtPreview.append("\n");
		txtPreview.append("\n");
		txtPreview
				.append("---------------------------------------------------------------------\n");
		txtPreview.append("                       Tracklisting\n");
		txtPreview
				.append("---------------------------------------------------------------------\n");
		txtPreview.append("\n");
		for (int i = 0; i < tableOfFiles.getItemCount(); i++) {
			int j = i + 1;
			txtPreview.append("   " + j + ". ("
					+ tableOfFiles.getItem(i).getText(5) + ") "
					+ tableOfFiles.getItem(i).getText(1) + " - "
					+ tableOfFiles.getItem(i).getText(2) + "\n");
		}
		txtPreview.append("\n");
		txtPreview.append("Playing Time.........: "
				+ txtSumOfTimeTracks.getText() + "\n");
		txtPreview.append("Total Size...........: " + txtSumLenght.getText()
				+ "\n");
		txtPreview.append("\n");
		txtPreview.append("NFO generated on.....: "
				+ new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
						.format(new Date()) + "\n");
		txtPreview.append("\n");
		txtPreview.append("\n");
		txtPreview
				.append("---------------------------------------------------------------------\n");
		txtPreview.append(txtAttachNote.getText() + "\n");
		txtPreview
				.append("---------------------------------------------------------------------\n");
		txtPreview.append("\n");
		txtPreview.append(":: Generated by jMusic NFO Builder v1.0 ::\n");
	}

	public void informationCTab() {
		cTabItemInformation = new CTabItem(cTabFolder, SWT.NONE);
		cTabItemInformation.setText(Messages.JMNB_CTabInformationText);
		cTabItemInformation
				.setToolTipText(Messages.JMNB_CTabInformationToolTipText);
		Composite composite = new Composite(cTabFolder, SWT.NONE);
		{
			FormLayout layout = new FormLayout();
			layout.marginTop = 5;
			layout.marginLeft = 5;
			layout.marginRight = 5;
			layout.spacing = 5;
			composite.setLayout(layout);
			FormData data = new FormData();
			data.right = new FormAttachment(0);
			data.left = new FormAttachment(100);
			composite.setLayoutData(data);
		}

		tableOfFiles = new Table(composite, SWT.VIRTUAL | SWT.MULTI
				| SWT.FULL_SELECTION | SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);

		TableColumn tblClmnFile = new TableColumn(tableOfFiles, SWT.NONE);
		tblClmnFile.setWidth(100);
		tblClmnFile.setText(Messages.JMNB_FileNameTableColumnText);

		TableColumn tblClmnAuthor = new TableColumn(tableOfFiles, SWT.NONE);
		tblClmnAuthor.setWidth(100);
		tblClmnAuthor.setText(Messages.JMNB_AuthorTableColumnText);

		TableColumn tblClmnTitle = new TableColumn(tableOfFiles, SWT.NONE);
		tblClmnTitle.setWidth(100);
		tblClmnTitle.setText(Messages.JMNB_TitleTableColumnText);

		TableColumn tblClmnAlbum = new TableColumn(tableOfFiles, SWT.NONE);
		tblClmnAlbum.setWidth(100);
		tblClmnAlbum.setText(Messages.JMNB_AlbumTableColumnText);

		TableColumn tblClmnLenght = new TableColumn(tableOfFiles, SWT.NONE);
		tblClmnLenght.setWidth(100);
		tblClmnLenght.setText(Messages.JMNB_LenghtTableColumnText);

		TableColumn tblClmnTime = new TableColumn(tableOfFiles, SWT.NONE);
		tblClmnTime.setWidth(100);
		tblClmnTime.setText(Messages.JMNB_TimeTableColumnText);

		TableColumn tblClmnBitrate = new TableColumn(tableOfFiles, SWT.NONE);
		tblClmnBitrate.setWidth(100);
		tblClmnBitrate.setText(Messages.JMNB_BitrateTableColumnText);

		tableOfFiles.setLinesVisible(true);
		tableOfFiles.setHeaderVisible(true);

		for (int i = 0; i < tableOfFiles.getColumnCount(); i++) {
			tableOfFiles.getColumn(i).pack();
		}

		{
			FormData data = new FormData();
			data.left = new FormAttachment(0);
			data.right = new FormAttachment(100);
			data.top = new FormAttachment(0);
			data.bottom = new FormAttachment(50);
			tableOfFiles.setLayoutData(data);
		}

		Group group1 = new Group(composite, SWT.NONE);
		group1.setText(Messages.JMNB_group1_text_1);
		{
			FormLayout layout = new FormLayout();
			layout.marginTop = 5;
			layout.marginLeft = 5;
			layout.marginRight = 5;
			layout.spacing = 5;
			FormData data = new FormData();
			data.right = new FormAttachment(100);
			data.left = new FormAttachment(0);
			data.top = new FormAttachment(tableOfFiles, 5);
			data.bottom = new FormAttachment(100);
			group1.setLayoutData(data);
			group1.setLayout(layout);
		}

		Button button1 = new Button(group1, SWT.PUSH);
		button1.setText(Messages.JMNB_BtnNextText);
		{
			FormData data = new FormData();
			data.left = new FormAttachment(85);
			data.right = new FormAttachment(100);
			data.bottom = new FormAttachment(99);
			data.top = new FormAttachment(92);
			button1.setLayoutData(data);

			button1.addSelectionListener(new SelectionListener() {
				public void widgetSelected(SelectionEvent event) {
				}

				public void widgetDefaultSelected(SelectionEvent event) {
				}
			});
		}

		Label lblMpegLayer = new Label(group1, SWT.RIGHT);
		lblMpegLayer.setText(Messages.JMNB_LblMpegLayerText);
		{
			FormData data = new FormData();
			data.left = new FormAttachment(0, 10);
			data.top = new FormAttachment(0, 27);
			data.height = 23;
			data.width = 100;
			lblMpegLayer.setLayoutData(data);
		}

		txtMpegLayer = new Text(group1, SWT.BORDER);
		txtMpegLayer.setEditable(true);
		{
			FormData data = new FormData();
			data.bottom = new FormAttachment(lblMpegLayer, 0, SWT.BOTTOM);
			data.top = new FormAttachment(lblMpegLayer, 0, SWT.TOP);
			data.left = new FormAttachment(lblMpegLayer, 5);
			data.height = 23;
			data.width = 100;
			txtMpegLayer.setLayoutData(data);
		}

		Label lblSampleRate = new Label(group1, SWT.RIGHT);
		lblSampleRate.setText(Messages.JMNB_LblSampleRateText);
		{
			FormData data = new FormData();
			data.left = new FormAttachment(0, 10);
			data.top = new FormAttachment(0, 54);
			data.height = 23;
			data.width = 100;
			lblSampleRate.setLayoutData(data);
		}

		txtSampleRate = new Text(group1, SWT.BORDER);
		txtSampleRate.setEditable(true);
		{
			FormData data = new FormData();
			data.bottom = new FormAttachment(lblSampleRate, 0, SWT.BOTTOM);
			data.top = new FormAttachment(lblSampleRate, 0, SWT.TOP);
			data.left = new FormAttachment(lblSampleRate, 5);
			data.height = 23;
			data.width = 100;
			txtSampleRate.setLayoutData(data);
		}

		Label lblChannels = new Label(group1, SWT.RIGHT);
		lblChannels.setText(Messages.JMNB_LblChannelsText);
		{
			FormData data = new FormData();
			data.left = new FormAttachment(0, 10);
			data.top = new FormAttachment(0, 81);
			data.height = 23;
			data.width = 100;
			lblChannels.setLayoutData(data);
		}

		txtChannels = new Text(group1, SWT.BORDER);
		txtChannels.setEditable(true);
		{
			FormData data = new FormData();
			data.bottom = new FormAttachment(lblChannels, 0, SWT.BOTTOM);
			data.top = new FormAttachment(lblChannels, 0, SWT.TOP);
			data.left = new FormAttachment(lblChannels, 5);
			data.height = 23;
			data.width = 100;
			txtChannels.setLayoutData(data);
		}

		Label lblEncoder = new Label(group1, SWT.RIGHT);
		lblEncoder.setText(Messages.JMNB_LblEncoderText);
		{
			FormData data = new FormData();
			data.left = new FormAttachment(0, 10);
			data.top = new FormAttachment(0, 108);
			data.height = 23;
			data.width = 100;
			lblEncoder.setLayoutData(data);
		}

		txtEncoder = new Text(group1, SWT.BORDER);
		txtEncoder.setEditable(true);
		{
			FormData data = new FormData();
			data.bottom = new FormAttachment(lblEncoder, 0, SWT.BOTTOM);
			data.top = new FormAttachment(lblEncoder, 0, SWT.TOP);
			data.left = new FormAttachment(lblEncoder, 5);
			data.height = 23;
			data.width = 100;
			txtEncoder.setLayoutData(data);
		}

		Label lblAvgBitrate = new Label(group1, SWT.RIGHT);
		lblAvgBitrate.setText(Messages.JMNB_LblAvgBitrateText);
		{
			FormData data = new FormData();
			data.left = new FormAttachment(0, 10);
			data.top = new FormAttachment(0, 135);
			data.height = 23;
			data.width = 100;
			lblAvgBitrate.setLayoutData(data);
		}

		txtAvgBitrate = new Text(group1, SWT.BORDER);
		txtAvgBitrate.setEditable(true);
		{
			FormData data = new FormData();
			data.bottom = new FormAttachment(lblAvgBitrate, 0, SWT.BOTTOM);
			data.top = new FormAttachment(lblAvgBitrate, 0, SWT.TOP);
			data.left = new FormAttachment(lblAvgBitrate, 5);
			data.height = 23;
			data.width = 100;
			txtAvgBitrate.setLayoutData(data);
		}

		Label lblQuality = new Label(group1, SWT.RIGHT);
		lblQuality.setText(Messages.JMNB_LblQualityText);
		{
			FormData data = new FormData();
			data.left = new FormAttachment(0, 10);
			data.top = new FormAttachment(0, 162);
			data.height = 23;
			data.width = 100;
			lblQuality.setLayoutData(data);
		}

		txtQuality = new Text(group1, SWT.BORDER);
		txtQuality.setEditable(true);
		txtQuality.setText(Messages.JMNB_TxtQualityText);
		{
			FormData data = new FormData();
			data.bottom = new FormAttachment(lblQuality, 0, SWT.BOTTOM);
			data.top = new FormAttachment(lblQuality, 0, SWT.TOP);
			data.left = new FormAttachment(lblQuality, 5);
			data.height = 23;
			data.width = 100;
			txtQuality.setLayoutData(data);
		}

		Label lblGenres = new Label(group1, SWT.RIGHT);
		lblGenres.setText(Messages.JMNB_LblGenresText);
		{
			FormData data = new FormData();
			data.left = new FormAttachment(0, 10);
			data.top = new FormAttachment(0, 189);
			data.height = 23;
			data.width = 100;
			lblGenres.setLayoutData(data);
		}

		comboGenres = new Combo(group1, SWT.NONE);
		{
			FormData data = new FormData();
			data.bottom = new FormAttachment(lblGenres, 0, SWT.BOTTOM);
			data.top = new FormAttachment(lblGenres, 0, SWT.TOP);
			data.left = new FormAttachment(lblGenres, 5);
			data.height = 23;
			data.width = 85;
			comboGenres.setLayoutData(data);
			FilesOperations reader = new FilesOperations();
			comboGenres.setItems(reader.readingFile("genres.ini"));
			comboGenres.select(0);
			comboGenres.setVisibleItemCount(30);
		}

		Label lblSumOfTimeTracks = new Label(group1, SWT.RIGHT);
		lblSumOfTimeTracks.setText(Messages.JMNB_LblSumOfTimeTracksText);
		{
			FormData data = new FormData();
			data.bottom = new FormAttachment(txtMpegLayer, 0, SWT.BOTTOM);
			data.top = new FormAttachment(txtMpegLayer, 0, SWT.TOP);
			data.left = new FormAttachment(txtMpegLayer, 5);
			data.height = 23;
			data.width = 100;
			lblSumOfTimeTracks.setLayoutData(data);
		}

		txtSumOfTimeTracks = new Text(group1, SWT.BORDER);
		txtSumOfTimeTracks.setEditable(true);
		{
			FormData data = new FormData();
			data.bottom = new FormAttachment(lblSumOfTimeTracks, 0, SWT.BOTTOM);
			data.top = new FormAttachment(lblSumOfTimeTracks, 0, SWT.TOP);
			data.left = new FormAttachment(lblSumOfTimeTracks, 5);
			data.height = 23;
			data.width = 100;
			txtSumOfTimeTracks.setLayoutData(data);
		}

		Label lblSumLenght = new Label(group1, SWT.RIGHT);
		lblSumLenght.setText(Messages.JMNB_LblSumLenghtText);
		{
			FormData data = new FormData();
			data.bottom = new FormAttachment(txtSampleRate, 0, SWT.BOTTOM);
			data.top = new FormAttachment(txtSampleRate, 0, SWT.TOP);
			data.left = new FormAttachment(txtSampleRate, 5);
			data.height = 23;
			data.width = 100;
			lblSumLenght.setLayoutData(data);
		}

		txtSumLenght = new Text(group1, SWT.BORDER);
		txtSumLenght.setEditable(true);
		{
			FormData data = new FormData();
			data.bottom = new FormAttachment(lblSumLenght, 0, SWT.BOTTOM);
			data.top = new FormAttachment(lblSumLenght, 0, SWT.TOP);
			data.left = new FormAttachment(lblSumLenght, 5);
			data.height = 23;
			data.width = 100;
			txtSumLenght.setLayoutData(data);
		}

		Label lblYear = new Label(group1, SWT.RIGHT);
		lblYear.setText(Messages.JMNB_LblYearText);
		{
			FormData data = new FormData();
			data.bottom = new FormAttachment(txtChannels, 0, SWT.BOTTOM);
			data.top = new FormAttachment(txtChannels, 0, SWT.TOP);
			data.left = new FormAttachment(txtChannels, 5);
			data.height = 23;
			data.width = 100;
			lblYear.setLayoutData(data);
		}

		txtYear = new Text(group1, SWT.BORDER);
		txtYear.setEditable(true);
		{
			FormData data = new FormData();
			data.bottom = new FormAttachment(lblYear, 0, SWT.BOTTOM);
			data.top = new FormAttachment(lblYear, 0, SWT.TOP);
			data.left = new FormAttachment(lblYear, 5);
			data.height = 23;
			data.width = 100;
			txtYear.setLayoutData(data);
		}

		Label lblV1TagId = new Label(group1, SWT.RIGHT);
		lblV1TagId.setText(Messages.JMNB_LblV1TagIdText);
		{
			FormData data = new FormData();
			data.bottom = new FormAttachment(txtEncoder, 0, SWT.BOTTOM);
			data.top = new FormAttachment(txtEncoder, 0, SWT.TOP);
			data.left = new FormAttachment(txtEncoder, 5);
			data.height = 23;
			data.width = 100;
			lblV1TagId.setLayoutData(data);
		}

		txtV1TagId = new Text(group1, SWT.BORDER);
		txtV1TagId.setEditable(true);
		{
			FormData data = new FormData();
			data.bottom = new FormAttachment(lblV1TagId, 0, SWT.BOTTOM);
			data.top = new FormAttachment(lblV1TagId, 0, SWT.TOP);
			data.left = new FormAttachment(lblV1TagId, 5);
			data.height = 23;
			data.width = 100;
			txtV1TagId.setLayoutData(data);
		}

		Label lblV2TagId = new Label(group1, SWT.RIGHT);
		lblV2TagId.setText(Messages.JMNB_LblV2TagIdText);
		{
			FormData data = new FormData();
			data.bottom = new FormAttachment(txtAvgBitrate, 0, SWT.BOTTOM);
			data.top = new FormAttachment(txtAvgBitrate, 0, SWT.TOP);
			data.left = new FormAttachment(txtAvgBitrate, 5);
			data.height = 23;
			data.width = 100;
			lblV2TagId.setLayoutData(data);
		}

		txtV2TagId = new Text(group1, SWT.BORDER);
		txtV2TagId.setEditable(true);
		{
			FormData data = new FormData();
			data.bottom = new FormAttachment(lblV2TagId, 0, SWT.BOTTOM);
			data.top = new FormAttachment(lblV2TagId, 0, SWT.TOP);
			data.left = new FormAttachment(lblV2TagId, 5);
			data.height = 23;
			data.width = 100;
			txtV2TagId.setLayoutData(data);
		}

		Label label13 = new Label(group1, SWT.RIGHT);
		label13.setText(Messages.JMNB_label13Text);
		{
			FormData data = new FormData();
			data.bottom = new FormAttachment(txtQuality, 0, SWT.BOTTOM);
			data.top = new FormAttachment(txtQuality, 0, SWT.TOP);
			data.left = new FormAttachment(txtQuality, 5);
			data.height = 23;
			data.width = 100;
			label13.setLayoutData(data);
		}

		Text text13 = new Text(group1, SWT.BORDER);
		text13.setEditable(true);
		{
			FormData data = new FormData();
			data.bottom = new FormAttachment(label13, 0, SWT.BOTTOM);
			data.top = new FormAttachment(label13, 0, SWT.TOP);
			data.left = new FormAttachment(label13, 5);
			data.height = 23;
			data.width = 100;
			text13.setLayoutData(data);
		}

		Label label14 = new Label(group1, SWT.RIGHT);
		label14.setText(Messages.JMNB_label14Text);
		{
			FormData data = new FormData();
			data.bottom = new FormAttachment(comboGenres, 0, SWT.BOTTOM);
			data.top = new FormAttachment(comboGenres, 0, SWT.TOP);
			data.left = new FormAttachment(comboGenres, 5);
			data.height = 23;
			data.width = 100;
			label14.setLayoutData(data);
		}

		Text text14 = new Text(group1, SWT.BORDER);
		text14.setEditable(true);
		{
			FormData data = new FormData();
			data.bottom = new FormAttachment(label14, 0, SWT.BOTTOM);
			data.top = new FormAttachment(label14, 0, SWT.TOP);
			data.left = new FormAttachment(label14, 5);
			data.height = 23;
			data.width = 100;
			text14.setLayoutData(data);
		}

		btnVariousArtists = new Button(group1, SWT.CHECK | SWT.RIGHT);
		{
			FormData data = new FormData();
			data.bottom = new FormAttachment(txtSumOfTimeTracks, 0, SWT.BOTTOM);
			data.top = new FormAttachment(txtSumOfTimeTracks, 0, SWT.TOP);
			data.left = new FormAttachment(txtSumOfTimeTracks, 115);
			data.height = 15;
			data.width = 15;
			btnVariousArtists.setLayoutData(data);
			btnVariousArtists.setSelection(true);
			btnVariousArtists.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent event) {
				}
			});
		}

		Label lblVariousArtists = new Label(group1, SWT.LEFT);
		lblVariousArtists.setText(Messages.JMNB_LblVariousArtistsText);
		{
			FormData data = new FormData();
			data.bottom = new FormAttachment(btnVariousArtists, 0, SWT.BOTTOM);
			data.top = new FormAttachment(btnVariousArtists, 3, SWT.TOP);
			data.left = new FormAttachment(btnVariousArtists, 1);
			data.height = 23;
			data.width = 100;
			lblVariousArtists.setLayoutData(data);
		}

		txtAlbum = new Text(group1, SWT.BORDER);
		txtAlbum.setEditable(true);
		{
			FormData data = new FormData();
			// data.bottom = new FormAttachment(button2, 0, SWT.BOTTOM);
			data.top = new FormAttachment(btnVariousArtists);
			data.left = new FormAttachment(txtSumLenght, 115);
			data.height = 15;
			data.width = 115;
			txtAlbum.setLayoutData(data);
			txtAlbum.addModifyListener(new ModifyListener() {
				public void modifyText(ModifyEvent event) {
					handleTxtAlbumModify(event);
				}
			});
		}

		Button btnNMR = new Button(group1, SWT.CHECK | SWT.RIGHT);
		{
			FormData data = new FormData();
			// data.bottom = new FormAttachment(text8, 0, SWT.BOTTOM);
			data.top = new FormAttachment(txtAlbum, 15);
			data.left = new FormAttachment(txtYear, 115);
			data.height = 15;
			data.width = 15;
			btnNMR.setLayoutData(data);
			btnNMR.addSelectionListener(new SelectionListener() {
				public void widgetSelected(SelectionEvent event) {
				}

				public void widgetDefaultSelected(SelectionEvent event) {
				}
			});
		}

		Label lblNMR = new Label(group1, SWT.LEFT);
		lblNMR.setText(Messages.JMNB_LblNMRText);
		{
			FormData data = new FormData();
			// data.bottom = new FormAttachment(button2, 0, SWT.BOTTOM);
			data.top = new FormAttachment(txtAlbum, 15);
			data.left = new FormAttachment(btnNMR);
			data.height = 23;
			data.width = 100;
			lblNMR.setLayoutData(data);
		}

		Label lblRipper = new Label(group1, SWT.RIGHT);
		lblRipper.setText(Messages.JMNB_LblRipperText);
		{
			FormData data = new FormData();
			data.bottom = new FormAttachment(txtV2TagId, 0, SWT.BOTTOM);
			data.top = new FormAttachment(txtV2TagId, 0, SWT.TOP);
			data.left = new FormAttachment(txtV2TagId, 5);
			data.height = 23;
			data.width = 100;
			lblRipper.setLayoutData(data);
		}

		comboRipper = new Combo(group1, SWT.NONE);
		{
			FormData data = new FormData();
			data.bottom = new FormAttachment(lblRipper, 0, SWT.BOTTOM);
			data.top = new FormAttachment(lblRipper, 0, SWT.TOP);
			data.left = new FormAttachment(lblRipper, 5);
			data.height = 23;
			data.width = 100;
			comboRipper.setLayoutData(data);
			comboRipper.setItems(new FilesOperations()
					.readingFile("rippers.ini"));
			comboRipper.select(0);
			comboRipper.setVisibleItemCount(30);
			comboRipper.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(final SelectionEvent event) {
					handleComboRipperSelected(event);
				}
			});
			comboRipper.addModifyListener(new ModifyListener() {
				public void modifyText(final ModifyEvent event) {
					handleComboRipperModify(event);
				}
			});
		}

		Label lblModelCD_DVD = new Label(group1, SWT.RIGHT);
		lblModelCD_DVD.setText(Messages.JMNB_LblModelCD_DVDText);
		{
			FormData data = new FormData();
			data.bottom = new FormAttachment(text13, 0, SWT.BOTTOM);
			data.top = new FormAttachment(text13, 0, SWT.TOP);
			data.left = new FormAttachment(text13, 5);
			data.height = 23;
			data.width = 100;
			lblModelCD_DVD.setLayoutData(data);
		}

		comboModelCD_DVD = new Combo(group1, SWT.NONE);
		{
			FormData data = new FormData();
			data.bottom = new FormAttachment(lblModelCD_DVD, 0, SWT.BOTTOM);
			data.top = new FormAttachment(lblModelCD_DVD, 0, SWT.TOP);
			data.left = new FormAttachment(lblModelCD_DVD, 5);
			data.height = 23;
			data.width = 100;
			comboModelCD_DVD.setLayoutData(data);
			comboModelCD_DVD.setItems(new FilesOperations()
					.readingFile("cd_dvd.ini"));
			comboModelCD_DVD.select(0);
			comboModelCD_DVD.setVisibleItemCount(30);
			comboModelCD_DVD.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(final SelectionEvent event) {
					handleComboModelCD_DVDSelected(event);
				}
			});
		}

		Label lblSources = new Label(group1, SWT.RIGHT);
		lblSources.setText(Messages.JMNB_LblSourcesText);
		{
			FormData data = new FormData();
			data.bottom = new FormAttachment(text14, 0, SWT.BOTTOM);
			data.top = new FormAttachment(text14, 0, SWT.TOP);
			data.left = new FormAttachment(text14, 5);
			data.height = 23;
			data.width = 100;
			lblSources.setLayoutData(data);
		}

		comboSources = new Combo(group1, SWT.NONE);
		{
			FormData data = new FormData();
			data.bottom = new FormAttachment(lblSources, 0, SWT.BOTTOM);
			data.top = new FormAttachment(lblSources, 0, SWT.TOP);
			data.left = new FormAttachment(lblSources, 5);
			data.height = 23;
			data.width = 100;
			comboSources.setLayoutData(data);
			FilesOperations reader = new FilesOperations();
			comboSources.setItems(reader.readingFile("sources.ini"));
			comboSources.select(0);
			comboSources.setVisibleItemCount(10);
			comboSources.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(final SelectionEvent event) {
					handleComboSourcesSelected(event);
				}
			});
			comboSources.addModifyListener(new ModifyListener() {
				public void modifyText(final ModifyEvent event) {
					handleComboSourcesModify(event);
				}
			});
		}

		cTabItemInformation.setControl(composite);
	}

	public void addonsCTab() {
		CTabItem tab = new CTabItem(cTabFolder, SWT.NONE);
		tab.setText(Messages.JMNB_tab_text_1);
		tab.setToolTipText(Messages.JMNB_tab_toolTipText_1);

		Composite composite = new Composite(cTabFolder, SWT.NONE);
		{
			FormLayout layout = new FormLayout();
			layout.marginTop = 5;
			layout.marginLeft = 5;
			layout.marginRight = 5;
			layout.spacing = 5;
			composite.setLayout(layout);
			FormData data = new FormData();
			data.right = new FormAttachment(0);
			data.left = new FormAttachment(100);
			composite.setLayoutData(data);
		}

		Group group1 = new Group(composite, SWT.NONE);
		group1.setText(Messages.JMNB_group1_text_2);
		{
			FormLayout layout = new FormLayout();
			layout.marginTop = 5;
			layout.marginLeft = 5;
			layout.marginRight = 5;
			layout.spacing = 5;
			FormData data = new FormData();
			data.top = new FormAttachment(0, 5);
			data.bottom = new FormAttachment(75);
			data.left = new FormAttachment(0);
			data.right = new FormAttachment(65);
			group1.setLayoutData(data);
			group1.setLayout(layout);
		}

		Label lblRipper = new Label(group1, SWT.NORMAL);
		lblRipper.setText(Messages.JMNB_lblRipper_text_1);
		{
			FormLayout layout = new FormLayout();
			layout.marginTop = 5;
			layout.marginLeft = 5;
			layout.marginRight = 5;
			layout.spacing = 5;
			FormData data = new FormData();
			data.left = new FormAttachment(group1);
			data.top = new FormAttachment(group1);
			lblRipper.setLayoutData(data);
		}

		txtRipper = new Text(group1, SWT.BORDER);
		{
			FormLayout layout = new FormLayout();
			layout.marginTop = 5;
			layout.marginLeft = 5;
			layout.marginRight = 5;
			layout.spacing = 5;
			FormData data = new FormData();
			data.left = new FormAttachment(lblRipper);
			data.top = new FormAttachment(lblRipper, 0, SWT.TOP);
			txtRipper.setLayoutData(data);
			txtRipper.addModifyListener(new ModifyListener() {
				public void modifyText(final ModifyEvent event) {
					handleTxtRipperModify(event);
				}
			});
			if (new FilesOperations().getCfgProperty(JMediaNFOCreator.PROGRAM_NAME + ".xml",
					"rippedby") != null) {
				txtRipper.setText(new FilesOperations().getCfgProperty(
						JMediaNFOCreator.PROGRAM_NAME + ".xml", "rippedby"));
			} else {
				new FilesOperations().setCfgProperty(
						JMediaNFOCreator.PROGRAM_NAME + ".xml", "rippedby", "Somebody");
				txtRipper.setText("Somebody");
			}
		}

		Label lblDateRip = new Label(group1, SWT.CENTER);
		lblDateRip.setText(Messages.JMNB_LblDateRipText);
		{
			FormLayout layout = new FormLayout();
			layout.marginTop = 5;
			layout.marginLeft = 5;
			layout.marginRight = 5;
			layout.spacing = 5;
			FormData data = new FormData();
			data.left = new FormAttachment(txtRipper);
			data.top = new FormAttachment(lblRipper, 0, SWT.TOP);
			lblDateRip.setLayoutData(data);
		}

		calComboDateRip = new CalendarCombo(group1, SWT.NONE);
		Calendar calendar = Calendar.getInstance(Locale.getDefault());
		calComboDateRip.setDate(calendar);

		{
			FormLayout layout = new FormLayout();
			layout.marginTop = 5;
			layout.marginLeft = 5;
			layout.marginRight = 5;
			layout.spacing = 5;
			FormData data = new FormData();
			data.left = new FormAttachment(lblDateRip);
			data.top = new FormAttachment(lblRipper, 0, SWT.TOP);
			calComboDateRip.setLayoutData(data);
		}

		Label lblPoster = new Label(group1, SWT.CENTER);
		lblPoster.setText(Messages.JMNB_LblPosterText);
		{
			FormLayout layout = new FormLayout();
			layout.marginTop = 5;
			layout.marginLeft = 5;
			layout.marginRight = 5;
			layout.spacing = 5;
			FormData data = new FormData();
			data.left = new FormAttachment(group1);
			data.top = new FormAttachment(15);
			lblPoster.setLayoutData(data);
		}

		txtPoster = new Text(group1, SWT.BORDER);
		{
			FormLayout layout = new FormLayout();
			layout.marginTop = 5;
			layout.marginLeft = 5;
			layout.marginRight = 5;
			layout.spacing = 5;
			FormData data = new FormData();
			data.left = new FormAttachment(lblPoster);
			data.top = new FormAttachment(lblPoster, 0, SWT.TOP);
			txtPoster.setLayoutData(data);
			txtPoster.addModifyListener(new ModifyListener() {
				public void modifyText(final ModifyEvent event) {
					handleTxtPosterModify(event);
				}
			});
			if (new FilesOperations().getCfgProperty(JMediaNFOCreator.PROGRAM_NAME + ".xml",
					"postedby") != null) {
				txtPoster.setText(new FilesOperations().getCfgProperty(
						JMediaNFOCreator.PROGRAM_NAME + ".xml", "postedby"));
			} else {
				new FilesOperations().setCfgProperty(
						JMediaNFOCreator.PROGRAM_NAME + ".xml", "postedby", "Somebody");
				txtPoster.setText("Somebody");
			}
		}

		Label lblDatePost = new Label(group1, SWT.CENTER);
		lblDatePost.setText(Messages.JMNB_LblDatePostText);
		{
			FormLayout layout = new FormLayout();
			layout.marginTop = 5;
			layout.marginLeft = 5;
			layout.marginRight = 5;
			layout.spacing = 5;
			FormData data = new FormData();
			data.left = new FormAttachment(txtPoster);
			data.top = new FormAttachment(lblPoster, 0, SWT.TOP);
			lblDatePost.setLayoutData(data);
		}

		calComboDatePost = new CalendarCombo(group1, SWT.NONE);
		calComboDatePost.setDate(Calendar.getInstance(Locale.getDefault()));

		{
			FormLayout layout = new FormLayout();
			layout.marginTop = 5;
			layout.marginLeft = 5;
			layout.marginRight = 5;
			layout.spacing = 5;
			FormData data = new FormData();
			data.left = new FormAttachment(lblDatePost);
			data.top = new FormAttachment(lblPoster, 0, SWT.TOP);
			calComboDatePost.setLayoutData(data);
		}

		Label lblInfoOfDisc = new Label(group1, SWT.LEFT);
		lblInfoOfDisc.setText(Messages.JMNB_LblInfoOfDiscText);
		{
			FormLayout layout = new FormLayout();
			layout.marginTop = 5;
			layout.marginLeft = 5;
			layout.marginRight = 5;
			layout.spacing = 5;
			FormData data = new FormData();
			data.left = new FormAttachment(0);
			data.right = new FormAttachment(txtInfoOfDisc, 50);
			data.top = new FormAttachment(30);
			data.bottom = new FormAttachment(40);
			lblInfoOfDisc.setLayoutData(data);
		}

		txtInfoOfDisc = new Text(group1, SWT.BORDER);
		{
			FormLayout layout = new FormLayout();
			layout.marginTop = 5;
			layout.marginLeft = 5;
			layout.marginRight = 5;
			layout.spacing = 5;
			FormData data = new FormData();
			data.left = new FormAttachment(lblInfoOfDisc, 50);
			data.right = new FormAttachment(100);
			data.top = new FormAttachment(30);
			data.bottom = new FormAttachment(40);
			txtInfoOfDisc.setLayoutData(data);
		}

		Label lblServerNews = new Label(group1, SWT.CENTER);
		lblServerNews.setText(Messages.JMNB_LblServerNewsText);
		{
			FormLayout layout = new FormLayout();
			layout.marginTop = 5;
			layout.marginLeft = 5;
			layout.marginRight = 5;
			layout.spacing = 5;
			FormData data = new FormData();
			data.left = new FormAttachment(0);
			data.right = new FormAttachment(comboServerNews, 50);
			data.top = new FormAttachment(45);
			data.bottom = new FormAttachment(55);
			lblServerNews.setLayoutData(data);
		}

		comboServerNews = new Combo(group1, SWT.CENTER);
		{
			FormLayout layout = new FormLayout();
			layout.marginTop = 5;
			layout.marginLeft = 5;
			layout.marginRight = 5;
			layout.spacing = 5;
			FormData data = new FormData();
			data.left = new FormAttachment(lblServerNews, 50);
			data.right = new FormAttachment(100);
			data.top = new FormAttachment(45);
			data.bottom = new FormAttachment(55);
			comboServerNews.setLayoutData(data);
			comboServerNews.setItems(new FilesOperations()
					.readingFile("servers.ini"));
			comboServerNews.select(0);
			comboServerNews.setVisibleItemCount(10);
			comboServerNews.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(final SelectionEvent event) {
					handleComboServerNewsSelected(event);
				}
			});
		}

		Label lblNewsGroup = new Label(group1, SWT.LEFT);
		lblNewsGroup.setText(Messages.JMNB_LblNewsGroupText);
		{
			FormLayout layout = new FormLayout();
			layout.marginTop = 5;
			layout.marginLeft = 5;
			layout.marginRight = 5;
			layout.spacing = 5;
			FormData data = new FormData();
			data.left = new FormAttachment(0);
			data.right = new FormAttachment(50);
			data.top = new FormAttachment(60);
			data.bottom = new FormAttachment(70);
			lblNewsGroup.setLayoutData(data);
		}

		comboNewsGroup = new Combo(group1, SWT.LEFT);
		{
			FormLayout layout = new FormLayout();
			layout.marginTop = 5;
			layout.marginLeft = 5;
			layout.marginRight = 5;
			layout.spacing = 5;
			FormData data = new FormData();
			data.left = new FormAttachment(50);
			data.right = new FormAttachment(95);
			data.top = new FormAttachment(60);
			data.bottom = new FormAttachment(70);
			comboNewsGroup.setLayoutData(data);
			comboNewsGroup.setItems(new FilesOperations()
					.readingFile("groups.ini"));
			comboNewsGroup.select(1);
			comboNewsGroup.setVisibleItemCount(20);
			comboNewsGroup.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(final SelectionEvent event) {
					handleComboNewsGroupSelected(event);
				}
			});
		}

		btnAddGroupPlus = new Button(group1, SWT.PUSH | SWT.CENTER);
		btnAddGroupPlus.setText(Messages.JMNB_BtnAddGroupPlusText);
		{
			FormLayout layout = new FormLayout();
			layout.marginTop = 5;
			layout.marginLeft = 5;
			layout.marginRight = 5;
			layout.spacing = 5;
			FormData data = new FormData();
			data.left = new FormAttachment(comboNewsGroup);
			data.top = new FormAttachment(comboNewsGroup, 0, SWT.TOP);
			btnAddGroupPlus.setLayoutData(data);
			btnAddGroupPlus.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(final SelectionEvent event) {
					handleComboNewsGroupSelected(event);
				}
			});
		}

		Label lblSelectedGroups = new Label(group1, SWT.LEFT);
		lblSelectedGroups.setText(Messages.JMNB_LblSelectedGroupsText);
		{
			FormLayout layout = new FormLayout();
			layout.marginTop = 5;
			layout.marginLeft = 5;
			layout.marginRight = 5;
			layout.spacing = 5;
			FormData data = new FormData();
			data.left = new FormAttachment(0);
			data.right = new FormAttachment(50);
			data.top = new FormAttachment(75);
			data.bottom = new FormAttachment(85);
			lblSelectedGroups.setLayoutData(data);
		}

		listGroups = new List(group1, SWT.LEFT | SWT.V_SCROLL | SWT.MULTI
				| SWT.BORDER);
		{
			FormLayout layout = new FormLayout();
			layout.marginTop = 5;
			layout.marginLeft = 5;
			layout.marginRight = 5;
			layout.spacing = 5;
			FormData data = new FormData();
			data.left = new FormAttachment(50);
			data.right = new FormAttachment(95);
			data.top = new FormAttachment(75);
			data.bottom = new FormAttachment(90);
			listGroups.setLayoutData(data);
			listGroups.select(0);
			listGroups.addMouseListener(new MouseAdapter() {
				public void mouseDoubleClick(final MouseEvent event) {
					handleListGroupsMouseDoubleClick(event);
				}
			});
		}

		btnRemoveGroupsMinus = new Button(group1, SWT.PUSH | SWT.CENTER);
		btnRemoveGroupsMinus.setText(Messages.JMNB_BtnRemoveGroupsMinusText);
		{
			FormLayout layout = new FormLayout();
			layout.marginTop = 5;
			layout.marginLeft = 5;
			layout.marginRight = 5;
			layout.spacing = 5;
			FormData data = new FormData();
			data.left = new FormAttachment(listGroups);
			data.top = new FormAttachment(listGroups, 0, SWT.TOP);
			btnRemoveGroupsMinus.setLayoutData(data);
			btnRemoveGroupsMinus.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(final SelectionEvent event) {
					handleListGroupsSelected(event);
				}
			});
		}

		Label lblFilenames = new Label(group1, SWT.LEFT);
		lblFilenames.setText(Messages.JMNB_LblFilenamesText);
		{
			FormLayout layout = new FormLayout();
			layout.marginTop = 5;
			layout.marginLeft = 5;
			layout.marginRight = 5;
			layout.spacing = 5;
			FormData data = new FormData();
			data.left = new FormAttachment(0);
			data.right = new FormAttachment(50);
			data.top = new FormAttachment(95);
			data.bottom = new FormAttachment(100);
			lblFilenames.setLayoutData(data);
		}

		txtFilenames = new Text(group1, SWT.LEFT | SWT.BORDER);
		{
			FormLayout layout = new FormLayout();
			layout.marginTop = 5;
			layout.marginLeft = 5;
			layout.marginRight = 5;
			layout.spacing = 5;
			FormData data = new FormData();
			data.left = new FormAttachment(50);
			data.right = new FormAttachment(90);
			data.top = new FormAttachment(95);
			data.bottom = new FormAttachment(100);
			txtFilenames.setLayoutData(data);
		}

		lblNFO = new Label(group1, SWT.CENTER);
		lblNFO.setText(Messages.JMNB_LblNFOText);
		{
			FormLayout layout = new FormLayout();
			layout.marginTop = 5;
			layout.marginLeft = 5;
			layout.marginRight = 5;
			layout.spacing = 5;
			FormData data = new FormData();
			data.left = new FormAttachment(90);
			data.right = new FormAttachment(100);
			data.top = new FormAttachment(95);
			data.bottom = new FormAttachment(100);
			lblNFO.setLayoutData(data);
		}

		Group group2 = new Group(composite, SWT.NONE);
		group2.setText(Messages.JMNB_group2_text_1);
		{
			FormLayout layout = new FormLayout();
			layout.marginTop = 5;
			layout.marginLeft = 5;
			layout.marginRight = 5;
			layout.spacing = 5;
			FormData data = new FormData();
			data.left = new FormAttachment(group1, 0, SWT.LEFT);
			data.right = new FormAttachment(group1, 0, SWT.RIGHT);
			data.top = new FormAttachment(75);
			data.bottom = new FormAttachment(100, -5);
			group2.setLayoutData(data);
			group2.setLayout(layout);
		}

		btnAttachNote = new Button(group2, SWT.CHECK);
		btnAttachNote.setText(Messages.JMNB_BtnAttachNoteText);
		btnAttachNote.setSelection(true);
		{
			FormLayout layout = new FormLayout();
			layout.marginTop = 5;
			layout.marginLeft = 5;
			layout.marginRight = 5;
			layout.spacing = 5;
			FormData data = new FormData();
			data.left = new FormAttachment(0);
			data.right = new FormAttachment(100);
			data.top = new FormAttachment(5);
			data.bottom = new FormAttachment(20);
			btnAttachNote.setLayoutData(data);
			btnAttachNote.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent event) {
					handleBtnAttachNoteSelected(event);
				}
			});
		}

		txtAttachNote = new Text(group2, SWT.BORDER | SWT.MULTI);
		txtAttachNote.setText(Messages.JMNB_TxtAttachNoteText);
		{
			FormLayout layout = new FormLayout();
			layout.marginTop = 5;
			layout.marginLeft = 5;
			layout.marginRight = 5;
			layout.spacing = 5;
			FormData data = new FormData();
			data.left = new FormAttachment(0);
			data.right = new FormAttachment(100);
			data.top = new FormAttachment(30);
			data.bottom = new FormAttachment(95);
			txtAttachNote.setLayoutData(data);
		}

		Group group3 = new Group(composite, SWT.NONE);
		group3.setText(Messages.JMNB_group3_text);
		{
			FormLayout layout = new FormLayout();
			layout.marginTop = 5;
			layout.marginLeft = 5;
			layout.marginRight = 5;
			layout.spacing = 5;
			FormData data = new FormData();
			data.left = new FormAttachment(65, 10);
			data.right = new FormAttachment(90);
			data.top = new FormAttachment(group1, 0, SWT.TOP);
			data.bottom = new FormAttachment(35);
			group3.setLayoutData(data);
			group3.setLayout(layout);
		}

		btnChecksums = new Button(group3, SWT.CHECK);
		btnChecksums.setText(Messages.JMNB_BtnChecksumsText);
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
			data.bottom = new FormAttachment(10);
			btnChecksums.setLayoutData(data);
		}

		btnPlaylistPLS = new Button(group3, SWT.CHECK);
		btnPlaylistPLS.setText(Messages.JMNB_BtnPlaylistPLSText);
		{
			FormLayout layout = new FormLayout();
			layout.marginTop = 5;
			layout.marginLeft = 5;
			layout.marginRight = 5;
			layout.spacing = 5;
			FormData data = new FormData();
			data.left = new FormAttachment(0);
			data.right = new FormAttachment(100);
			data.top = new FormAttachment(10);
			data.bottom = new FormAttachment(20);
			btnPlaylistPLS.setLayoutData(data);
		}

		btnPlaylistM3U = new Button(group3, SWT.CHECK);
		btnPlaylistM3U.setText(Messages.JMNB_BtnPlaylistM3UText);
		{
			FormLayout layout = new FormLayout();
			layout.marginTop = 5;
			layout.marginLeft = 5;
			layout.marginRight = 5;
			layout.spacing = 5;
			FormData data = new FormData();
			data.left = new FormAttachment(0);
			data.right = new FormAttachment(100);
			data.top = new FormAttachment(20);
			data.bottom = new FormAttachment(30);
			btnPlaylistM3U.setLayoutData(data);
		}

		btnEACLog = new Button(group3, SWT.CHECK);
		btnEACLog.setText(Messages.JMNB_BtnEACLogText);
		{
			FormLayout layout = new FormLayout();
			layout.marginTop = 5;
			layout.marginLeft = 5;
			layout.marginRight = 5;
			layout.spacing = 5;
			FormData data = new FormData();
			data.left = new FormAttachment(0);
			data.right = new FormAttachment(100);
			data.top = new FormAttachment(30);
			data.bottom = new FormAttachment(40);
			btnEACLog.setLayoutData(data);
		}

		btnPAR = new Button(group3, SWT.CHECK);
		btnPAR.setText(Messages.JMNB_BtnPARText);
		{
			FormLayout layout = new FormLayout();
			layout.marginTop = 5;
			layout.marginLeft = 5;
			layout.marginRight = 5;
			layout.spacing = 5;
			FormData data = new FormData();
			data.left = new FormAttachment(0);
			data.right = new FormAttachment(50);
			data.top = new FormAttachment(40);
			data.bottom = new FormAttachment(50);
			btnPAR.setLayoutData(data);
			btnPAR.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent event) {
					if (btnPAR.getSelection() == true)
						btnPAR2.setSelection(false);
				}
			});
		}

		btnPAR2 = new Button(group3, SWT.CHECK);
		btnPAR2.setText(Messages.JMNB_BtnPAR2Text);
		{
			FormLayout layout = new FormLayout();
			layout.marginTop = 5;
			layout.marginLeft = 5;
			layout.marginRight = 5;
			layout.spacing = 5;
			FormData data = new FormData();
			data.left = new FormAttachment(50);
			data.right = new FormAttachment(100);
			data.top = new FormAttachment(40);
			data.bottom = new FormAttachment(50);
			btnPAR2.setLayoutData(data);
			btnPAR2.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent event) {
					if (btnPAR2.getSelection() == true)
						btnPAR.setSelection(false);
				}
			});
		}

		btnCUESheet = new Button(group3, SWT.CHECK);
		btnCUESheet.setText(Messages.JMNB_BtnCUESheetText);
		{
			FormLayout layout = new FormLayout();
			layout.marginTop = 5;
			layout.marginLeft = 5;
			layout.marginRight = 5;
			layout.spacing = 5;
			FormData data = new FormData();
			data.left = new FormAttachment(0);
			data.right = new FormAttachment(100);
			data.top = new FormAttachment(50);
			data.bottom = new FormAttachment(60);
			btnCUESheet.setLayoutData(data);
		}

		Group grpCovers = new Group(composite, SWT.NONE);
		grpCovers.setText(Messages.JMNB_grpCovers_text);
		{
			FormLayout layout = new FormLayout();
			layout.marginTop = 5;
			layout.marginLeft = 5;
			layout.marginRight = 5;
			layout.spacing = 5;
			FormData data = new FormData();
			data.left = new FormAttachment(90, 10);
			data.right = new FormAttachment(100);
			data.top = new FormAttachment(group1, 0, SWT.TOP);
			data.bottom = new FormAttachment(35);
			grpCovers.setLayoutData(data);
			grpCovers.setLayout(layout);
		}

		btnFront = new Button(grpCovers, SWT.CHECK);
		btnFront.setText(Messages.JMNB_BtnFrontText);
		{
			FormData data = new FormData();
			data.left = new FormAttachment(0);
			data.right = new FormAttachment(100);
			data.top = new FormAttachment(0);
			data.bottom = new FormAttachment(10);
			btnFront.setLayoutData(data);
		}

		btnBack = new Button(grpCovers, SWT.CHECK);
		btnBack.setText(Messages.JMNB_BtnBackText);
		{
			FormData data = new FormData();
			data.left = new FormAttachment(0);
			data.right = new FormAttachment(100);
			data.top = new FormAttachment(10);
			data.bottom = new FormAttachment(20);
			btnBack.setLayoutData(data);
		}

		btnCD = new Button(grpCovers, SWT.CHECK);
		btnCD.setText(Messages.JMNB_BtnCDText);
		{
			FormData data = new FormData();
			data.left = new FormAttachment(0);
			data.right = new FormAttachment(100);
			data.top = new FormAttachment(20);
			data.bottom = new FormAttachment(30);
			btnCD.setLayoutData(data);
		}

		Group group5 = new Group(composite, SWT.NONE);
		group5.setText(Messages.JMNB_group5_text);
		{
			FormLayout layout = new FormLayout();
			layout.marginTop = 5;
			layout.marginLeft = 5;
			layout.marginRight = 5;
			layout.spacing = 5;
			FormData data = new FormData();
			data.right = new FormAttachment(grpCovers, 0, SWT.RIGHT);
			data.top = new FormAttachment(group2, 0, SWT.TOP);
			data.bottom = new FormAttachment(100, -5);
			data.left = new FormAttachment(group2, 5);
			group5.setLayoutData(data);
			group5.setLayout(layout);
		}

		Label label9 = new Label(group5, SWT.CENTER);
		label9.setLayoutData(new FormData());
		label9.setText(Messages.JMNB_label9Text);

		tab.setControl(composite);
	}

	protected void handleTxtAlbumModify(final ModifyEvent event) {
		new FilesOperations().setCfgProperty("JMediaNFOCreator.xml", "VA",
				txtAlbum.getText());
	}

	protected void handleBtnAttachNoteSelected(final SelectionEvent event) {
		if (btnAttachNote.getSelection() == true) {
			new FilesOperations().setCfgProperty("JMediaNFOCreator.xml",
					"notes", "1");
		} else
			new FilesOperations().setCfgProperty("JMediaNFOCreator.xml",
					"notes", "0");
	}

	protected void handleComboRipperSelected(final SelectionEvent event) {
		new FilesOperations().setCfgProperty("JMediaNFOCreator.xml", "ripper",
				comboRipper.getText());
	}

	protected void handleComboRipperModify(final ModifyEvent event) {
		new FilesOperations().setCfgProperty("JMediaNFOCreator.xml", "ripper",
				comboRipper.getText());
	}

	protected void handleComboSourcesSelected(final SelectionEvent event) {
		new FilesOperations().setCfgProperty("JMediaNFOCreator.xml", "source",
				comboSources.getText());
	}

	protected void handleComboSourcesModify(final ModifyEvent event) {
		new FilesOperations().setCfgProperty("JMediaNFOCreator.xml", "source",
				comboSources.getText());
	}

	protected void handleComboModelCD_DVDSelected(final SelectionEvent event) {
		new FilesOperations().setCfgProperty("JMediaNFOCreator.xml", "cd/dvd",
				comboModelCD_DVD.getText());
	}

	protected void handleComboServerNewsSelected(final SelectionEvent event) {
		new FilesOperations().setCfgProperty("JMediaNFOCreator.xml", "server",
				comboServerNews.getText());
	}

	protected void handleComboNewsGroupSelected(final SelectionEvent event) {
		if (listGroups.indexOf(comboNewsGroup.getItem(comboNewsGroup
				.getSelectionIndex())) == -1) {
			listGroups.add(comboNewsGroup.getItem(comboNewsGroup
					.getSelectionIndex()));
		}
		new FilesOperations().setCfgProperty("JMediaNFOCreator.xml", "group",
				listGroups.getItems().toString());
	}

	protected void handleListGroupsMouseDoubleClick(final MouseEvent event) {
		listGroups.remove(listGroups.getSelectionIndices());
	}

	protected void handleListGroupsSelected(final SelectionEvent event) {
		listGroups.remove(listGroups.getSelectionIndices());
	}

	protected void handleTxtRipperModify(final ModifyEvent event) {
		new FilesOperations().setCfgProperty("JMediaNFOCreator.xml",
				"rippedby", txtRipper.getText());
	}

	protected void handleTxtPosterModify(final ModifyEvent event) {
		new FilesOperations().setCfgProperty("JMediaNFOCreator.xml",
				"postedby", txtPoster.getText());
	}

	public void previewTab() {
		cTabPreview = new CTabItem(cTabFolder, SWT.NONE);
		cTabPreview.setText("Podgląd");
		cTabPreview.setToolTipText("Podgląd");

		Composite composite = new Composite(cTabFolder, SWT.NONE);
		{
			FormLayout layout = new FormLayout();
			layout.marginTop = 5;
			layout.marginLeft = 5;
			layout.marginRight = 5;
			layout.spacing = 5;
			composite.setLayout(layout);
			FormData data = new FormData();
			data.right = new FormAttachment(0);
			data.left = new FormAttachment(100);
			composite.setLayoutData(data);
		}

		Button btnWriteNFO = new Button(composite, SWT.PUSH);
		btnWriteNFO.setText("Zapisz NFO");
		{
			FormData data = new FormData();
			data.left = new FormAttachment(0, 5);
			// data.right = new FormAttachment(100);
			data.top = new FormAttachment(0, 5);
			// data.bottom = new FormAttachment(100);
			btnWriteNFO.setLayoutData(data);
			btnWriteNFO.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent event) {
					handleBtnWriteNFOSelected(event);
				}
			});
		}

		txtPreview = new Text(composite, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL);
		txtPreview.setFont(new Font(txtPreview.getDisplay(), new FontData(
				"Courier New", 9, SWT.NORMAL)));
		{
			FormLayout layout = new FormLayout();
			layout.marginTop = 5;
			layout.marginLeft = 5;
			layout.marginRight = 5;
			layout.spacing = 5;
			FormData data = new FormData();
			data.left = new FormAttachment(0);
			data.right = new FormAttachment(100);
			data.top = new FormAttachment(btnWriteNFO, 5);
			data.bottom = new FormAttachment(100, -5);
			txtPreview.setLayoutData(data);
		}
		cTabPreview.setControl(composite);
	}

	protected void handleBtnWriteNFOSelected(final SelectionEvent event) {
		if (btnPlaylistPLS.getSelection() == true) {
			createPLSv2File();
		}
		if (btnPlaylistM3U.getSelection() == true) {
			createExtendedM3U8File();
		}
		try {
			BufferedWriter outputStream = new BufferedWriter(new FileWriter(
					new File(selectedDirectory + File.separatorChar
							+ txtFilenames.getText() + lblNFO.getText())));

			outputStream
					.write("---------------------------------------------------------------------");
			outputStream.newLine();
			outputStream
					.write("                      VA - Increase the Dosage");
			outputStream.newLine();
			outputStream
					.write("---------------------------------------------------------------------");
			outputStream.newLine();
			outputStream.newLine();
			if (btnVariousArtists.isEnabled() == true)
				outputStream.write("Artist...............: Various Artists");
			outputStream.newLine();
			outputStream.write("Album................: " + txtAlbum.getText());
			outputStream.newLine();
			outputStream.write("Genre................: "
					+ comboGenres.getText());
			outputStream.newLine();
			outputStream.write("Source...............: "
					+ comboSources.getText());
			outputStream.newLine();
			outputStream.write("Year.................: " + txtYear.getText());
			outputStream.newLine();
			outputStream.write("Ripper...............: "
					+ comboRipper.getText() + " & "
					+ comboModelCD_DVD.getText());
			outputStream.newLine();
			outputStream
					.write("Codec................: " + txtEncoder.getText());
			outputStream.newLine();
			outputStream.write("Version..............: "
					+ txtMpegLayer.getText());
			outputStream.newLine();
			outputStream.write("Quality..............: " + txtQuality.getText()
					+ ", (avg. bitrate: " + txtAvgBitrate.getText() + "kbps)");
			outputStream.newLine();
			outputStream.write("Channels.............: "
					+ txtChannels.getText() + " / " + txtSampleRate.getText());
			outputStream.newLine();
			outputStream.write("Tags.................: " + txtV1TagId.getText()
					+ ", " + txtV2TagId.getText());
			outputStream.newLine();
			outputStream.write("Information..........: "
					+ txtInfoOfDisc.getText());
			outputStream.newLine();
			outputStream.write("Ripped by............: " + txtRipper.getText()
					+ " on " + calComboDateRip.getDateAsString());
			outputStream.newLine();
			outputStream.write("Posted by............: " + txtPoster.getText()
					+ " on " + calComboDatePost.getDateAsString());
			outputStream.newLine();
			outputStream.write("News Server..........: "
					+ comboServerNews.getText());
			outputStream.newLine();
			outputStream.write("News Group(s)........: "
					+ comboNewsGroup.getText());
			outputStream.newLine();
			outputStream.write("Included.............: NFO");
			outputStream.newLine();
			outputStream.newLine();
			outputStream.newLine();
			outputStream
					.write("---------------------------------------------------------------------");
			outputStream.newLine();
			outputStream.write("                       Tracklisting");
			outputStream.newLine();
			outputStream
					.write("---------------------------------------------------------------------");
			outputStream.newLine();
			outputStream.newLine();
			for (int i = 0; i < tableOfFiles.getItemCount(); i++) {
				int j = i + 1;
				outputStream.write("   " + j + ". ("
						+ tableOfFiles.getItem(i).getText(5) + ") "
						+ tableOfFiles.getItem(i).getText(1) + " - "
						+ tableOfFiles.getItem(i).getText(2));
				outputStream.newLine();
			}
			outputStream.newLine();
			outputStream.write("Playing Time.........: "
					+ txtSumOfTimeTracks.getText());
			outputStream.newLine();
			outputStream.write("Total Size...........: "
					+ txtSumLenght.getText());
			outputStream.newLine();
			outputStream.newLine();
			outputStream.write("NFO generated on.....: "
					+ new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
							.format(new Date()));
			outputStream.newLine();
			outputStream.newLine();
			outputStream.newLine();
			outputStream
					.write("---------------------------------------------------------------------");
			outputStream.newLine();
			outputStream.write(txtAttachNote.getText());
			outputStream.newLine();
			outputStream
					.write("---------------------------------------------------------------------");
			outputStream.newLine();
			outputStream.newLine();
			outputStream.write(":: Generated by jMusic NFO Builder v1.0 ::");
			outputStream.close();

			if (btnChecksums.getSelection() == true) {
				createChecksumFile();
			}

			if (btnPlaylistPLS.getSelection() == true) {

			}

			if (btnPlaylistM3U.getSelection() == true) {

			}

			if (btnEACLog.getSelection() == true) {

			}

			if (btnPAR.getSelection() == true) {

			}

			if (btnPAR2.getSelection() == true) {

			}

			if (btnCUESheet.getSelection() == true) {

			}

			lblstateNFO.setText("NFO zapisane!");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void createChecksumFile() {
		if ((new FilesOperations().getCfgProperty("JMediaNFOCreator.xml",
				"autocrc").equals("1") == true)
				& (new FilesOperations().getCfgProperty("JMediaNFOCreator.xml",
						"crc").equals("MD5") == true)) {
			createChecksumMD5File();
		} else if ((new FilesOperations().getCfgProperty(
				"JMediaNFOCreator.xml", "autocrc").equals("1") == true)
				& (new FilesOperations().getCfgProperty("JMediaNFOCreator.xml",
						"crc").equals("SFV") == true)) {
			createChecksumSFVFile();
		}
	}

	private void createChecksumMD5File() {
		try {

			BufferedWriter outputStream = new BufferedWriter(new FileWriter(
					new File(selectedDirectory + File.separatorChar
							+ txtFilenames.getText() + ".MD5")));
			outputStream.write(";Generated by JMediaNFOCreator v1.20");
			outputStream.newLine();
			outputStream.write(";Unicode MD5-file created on "
					+ new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
							.format(new Date()));
			outputStream.newLine();
			outputStream.write(";");
			outputStream.newLine();
			for (int i = 0; i < tableOfFiles.getItemCount(); i++) {
				outputStream.write(tableOfFiles.getItem(i).getText(7) + " *"
						+ filterFiles.get(i));
				outputStream.newLine();
			}
			outputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void createChecksumSFVFile() {
		try {
			BufferedWriter outputStream = new BufferedWriter(new FileWriter(
					new File(selectedDirectory + File.separatorChar
							+ txtFilenames.getText() + ".SFV")));
			outputStream.write(";Generated by JMediaNFOCreator v1.20");
			outputStream.newLine();
			outputStream.write(";Unicode SFV-file created on "
					+ new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
							.format(new Date()));
			outputStream.newLine();
			outputStream.write(";");
			outputStream.newLine();
			for (int i = 0; i < tableOfFiles.getItemCount(); i++) {
				outputStream.write(filterFiles.get(i) + " "
						+ tableOfFiles.getItem(i).getText(7));
				outputStream.newLine();
			}
			outputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void createPLSv2File() {
		try {
			BufferedWriter outputStream = new BufferedWriter(new FileWriter(
					new File(selectedDirectory + File.separatorChar
							+ txtFilenames.getText() + ".pls")));
			outputStream.write("[playlist]");
			for (int i = 0; i < filterFiles.size(); i++) {
				int j = i + 1;
				outputStream.newLine();
				outputStream.write("File" + j + "=" + filterFiles.get(i));
				outputStream.newLine();
				outputStream.write("Title"
						+ j
						+ "="
						+ new TagsReader().getTagReader(selectedDirectory
								+ File.separatorChar + filterFiles.get(i),
								FieldKey.TITLE));
				outputStream.newLine();
				outputStream
						.write("Length"
								+ j
								+ "="
								+ Math.round(new TagsReader()
										.getTrackLength(selectedDirectory
												+ File.separatorChar
												+ filterFiles.get(i))));
			}
			outputStream.newLine();
			outputStream.write("NumberOfEntries=" + filterFiles.size());
			outputStream.newLine();
			outputStream.write("Version=2");
			outputStream.newLine();
			outputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void createExtendedM3U8File() {
		try {
			BufferedWriter outputStream = new BufferedWriter(new FileWriter(
					new File(selectedDirectory + File.separatorChar
							+ txtFilenames.getText() + ".m3u8")));
			outputStream.write("#EXTM3U");
			for (int i = 0; i < filterFiles.size(); i++) {
				outputStream.newLine();
				outputStream.write("#EXTINF:"
						+ Math.round(new TagsReader()
								.getTrackLength(selectedDirectory
										+ File.separatorChar + filterFiles.get(i)))
						+ ","
						+ new TagsReader().getTagReader(selectedDirectory
								+ File.separatorChar + filterFiles.get(i),
								FieldKey.TITLE));
				outputStream.newLine();
				outputStream.write(filterFiles.get(i));
			}
			outputStream.newLine();
			outputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void browserCTab() {
		CTabItem tab = new CTabItem(cTabFolder, SWT.NONE);
		tab.setText(Messages.JMNB_tab_text_2);
		tab.setToolTipText(Messages.JMNB_tab_toolTipText_2);
		Browser browser = null;
		try {
			browser = new Browser(cTabFolder, SWT.NONE);
		} catch (SWTError e) {
			Label label = new Label(cTabFolder, SWT.BORDER);
			label.setText("Nie można zainicjalizować przeglądarki");
			tab.setControl(label);
		}
		if (browser != null) {
			browser.setUrl("http://jmnc.sf.net/");
			tab.setControl(browser);
		}
	}
	
	public void open() {
		display = new Display();
		createContents();
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	public static void main(String[] args) {
		try {
			JMediaNFOCreator mainWindow = new JMediaNFOCreator();
			mainWindow.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}