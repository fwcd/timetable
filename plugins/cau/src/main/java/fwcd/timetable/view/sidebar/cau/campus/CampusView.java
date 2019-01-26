package fwcd.timetable.view.sidebar.cau.campus;

import fwcd.timetable.view.utils.FxView;

import javafx.scene.Node;
import javafx.scene.web.WebView;

public class CampusView implements FxView {
	private static final String URL ="https://umap.openstreetmap.fr/de/map/cau-baustellenplan_alle_224122?&scrollWheelZoom=true&zoomControl=true&allowEdit=false&moreControl=true&searchControl=null&tilelayersControl=null&embedControl=null&datalayersControl=true&onLoadPanel=undefined&captionBar=false";
	private final WebView node;
	private boolean loaded = false;
	
	public CampusView() {
		node = new WebView();
	}
	
	public void load() {
		if (!loaded) {
			node.getEngine().load(URL);
			loaded = true;
		}
	}
	
	@Override
	public Node getNode() { return node; }
}
