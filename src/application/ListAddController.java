package application;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.stage.FileChooser;

public class ListAddController implements Initializable {

	@FXML
	private ListView<String> listview;
	@FXML
	private TextField txt;
	@FXML
	private Button btn_add, btn_delete, btn_alldelete, btn_save, btn_open;
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
		fc.getExtensionFilters().addAll(
				new FileChooser.ExtensionFilter("CSV", "*.csv"),
				new FileChooser.ExtensionFilter("All", "*.*"),
				new FileChooser.ExtensionFilter("text", "*.txt"));
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
		if (selectedIdx != -1) {
			delete(selectedIdx);
		}
	}

	private void delete(final int selectedIdx) {
		String itemToRemove = listview.getSelectionModel().getSelectedItem();

		final int newSelectedIdx = (selectedIdx == listview.getItems().size() - 1)
				? selectedIdx - 1
				: selectedIdx;

		listview.getItems().remove(selectedIdx);
		listview.getSelectionModel().select(newSelectedIdx);
		System.out.println("selectIdx:" + selectedIdx);
		System.out.println("item:" + itemToRemove);
	}

	@FXML
	private void btn_alldelete_onClick(MouseEvent e) {
		items.clear();
	}

	@FXML
	private void btn_save_onClick(MouseEvent e) {
		File saveFile = fc.showSaveDialog(null);

		if (saveFile != null) {
			fileSave(saveFile,items);
		}
	}

	private void fileSave(File saveFile , ObservableList<String> items) {
		lbl.setText(saveFile.getPath().toString());
		try {
			FileWriter fw = new FileWriter(saveFile);
			BufferedWriter bw = new BufferedWriter(fw);
			for (String output : items) {
				bw.write(output);
				bw.write(",");
			}
			bw.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	@FXML
	private void btn_open_onClick(MouseEvent e) {
		File openFile = fc.showOpenDialog(null);

		if (openFile != null) {
			fileOpen(openFile, items);
		}
	}

	private void fileOpen(File openFile, ObservableList<String> items) {
		lbl.setText(openFile.getPath().toString());
		try {
			BufferedReader br = new BufferedReader(new FileReader(openFile));
			String line;
			while ((line = br.readLine()) != null) {
				items.addAll(line.split(",", 0));
				//for(String elem : list) {
				//items.add(elem);
				//}
			}
			br.close();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	private void setOnDragDetected(MouseEvent e) {
		Dragboard db = listview.startDragAndDrop(TransferMode.ANY);
		System.out.println("DragDetected");
		ClipboardContent content = new ClipboardContent();
		content.putString(listview.getSelectedText());
		db.setContent(content);
	}
}
