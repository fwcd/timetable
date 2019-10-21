package fwcd.timetable.view.utils.calendar;

import com.google.gson.Gson;

import fwcd.fructose.Option;
import fwcd.timetable.model.calendar.CalendarEntryModel;
import fwcd.timetable.model.calendar.CalendarGsonConfigurator;
import fwcd.timetable.model.json.GsonUtils;
import fwcd.timetable.view.FxView;
import fwcd.timetable.viewmodel.Localizer;
import fwcd.timetable.viewmodel.TemporalFormatters;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.VBox;

public class CalendarEntryCell implements FxView {
	private static final Gson CLIPBOARD_GSON = GsonUtils.DEFAULT_CONFIGURATOR.andThen(new CalendarGsonConfigurator()).create();
	private final Localizer localizer;
	private final TemporalFormatters formatters;
	
	private final Label titleLabel = new Label();
	private final Label subtitleLabel = new Label();
	private final VBox node;
	
	private Option<CalendarEntryModel> currentItem = Option.empty();
	private boolean showTitle = true;
	private boolean showSubtitle = true;
	
	public CalendarEntryCell(Localizer localizer, TemporalFormatters formatters) {
		this.localizer = localizer;
		this.formatters = formatters;
		
		titleLabel.getStyleClass().addAll("entry-cell-label", "title-cell-label");
		subtitleLabel.getStyleClass().addAll("entry-cell-label", "subtitle-cell-label");
		
		node = new VBox(titleLabel);
		node.getStyleClass().add("entry-cell");
		node.setOnDragDetected(e -> {
			if (currentItem.isPresent()) {
				Dragboard dragboard = node.startDragAndDrop(TransferMode.COPY);
				ClipboardContent content = new ClipboardContent();
				String json = CLIPBOARD_GSON.toJson(currentItem.unwrap());
				content.putString(json);
				dragboard.setContent(content);
			}
		});
	}
	
	public void setShowTitle(boolean showTitle) {
		this.showTitle = showTitle;
	}
	
	public void setShowSubtitle(boolean showTitle) {
		this.showTitle = showTitle;
	}
	
	public void updateItem(CalendarEntryModel item) {
		currentItem = Option.of(item);
		
		if (item == null) {
			node.getChildren().clear();
		} else {
			CalendarEntryInfoProvider infoProvider = new CalendarEntryInfoProvider(localizer, formatters);
			String info = item.accept(infoProvider);
			
			titleLabel.setText(titlePrefixOf(item) + item.getName());
			subtitleLabel.setText(info);
			
			if (showTitle) {
				if (showSubtitle && !info.isEmpty()) {
					node.getChildren().setAll(titleLabel, subtitleLabel);
				} else {
					node.getChildren().setAll(titleLabel);
				}
			} else if (showSubtitle) {
				node.getChildren().setAll(subtitleLabel);
			}
		}
	}
	
	private String titlePrefixOf(CalendarEntryModel entry) {
		return "";
	}
	
	@Override
	public Node getNode() { return node; }
}
