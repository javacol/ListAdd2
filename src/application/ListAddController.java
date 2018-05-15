package application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;

public class ListAddController implements Initializable{

	@FXML
	private ListView<String> listview;
	@FXML
	private TextField txt;
	@FXML
	private Button btn_add,btn_delete,btn_alldelete,btn_save,btn_open;
	@FXML
	private Label lbl;

	private ObservableList<String> items;

	private StringBuilder builder = new StringBuilder();

	final FileChooser fc = new FileChooser();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		items = FXCollections.observableArrayList();

		listview.setItems(items);
		listview.setCellFactory(TextFieldListCell.forListView());

		fc.setTitle("ファイル選択");

	}

	@FXML
	private void btn_add_onClick(MouseEvent e) {
		String str = txt.getText();

		items.add(str);

		items.stream().forEach((s) -> {
			System.out.println(s);
		});
	}

	@FXML
	private void btn_delete_onClick(MouseEvent e) {
		final int selectedIdx = listview.getSelectionModel().getSelectedIndex();
		if(selectedIdx != -1) {
			String itemToRemove = listview.getSelectionModel().getSelectedItem();

			final int newSelectedIdx = (selectedIdx == listview.getItems().size() - 1)
					? selectedIdx - 1 : selectedIdx;

			listview.getItems().remove(selectedIdx);
			listview.getSelectionModel().select(newSelectedIdx);
			System.out.println("selectIdx:" + selectedIdx);
			System.out.println("item:" + itemToRemove);
			//items.remove(selectedIdx);
		}
	}

	@FXML
	private void btn_alldelete_onClick(MouseEvent e) {
		items.clear();
	}

	@FXML
	private void btn_save_onClick(MouseEvent e) {
//		for(String str : items) {
//			builder.append(str).append(",");
//		}
//
//		String result = builder.toString();

		File saveFile = fc.showSaveDialog(null);

		if(saveFile != null) {
			lbl.setText(saveFile.getPath().toString());
		}

		fc.setInitialDirectory(new File("c:/2018_2nen/"));
		fc.setInitialFileName("save.txt");

	}

	@FXML
	private void btn_open_onClick(MouseEvent e) {
		FileFilter filter = new FileNameExtensionFilter("text","txt");
		fc.addChoosableFileFilter(filter);
		File openFile = fc.showOpenDialog(null);

		if(openFile != null) {
			lbl.setText(openFile.getPath().toString());
		}

		try {
			BufferedReader br =new BufferedReader(new FileReader(openFile));

			String line;
			while((line = br.readLine()) != null) {
				items.addAll(line.split(",", 0));
//				for(String elem : list) {
//					items.add(elem);
//				}
			}
			br.close();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}


}
