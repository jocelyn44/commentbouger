package nantes.tan;

public class Shapes {


	public String shape_id;
	public String shape_pt_lat;
	public String shape_pt_lon;
	public String shape_pt_sequence;
	public Shapes() {
		super();
	}
	public Shapes(String shape_id, String shape_pt_lat, String shape_pt_lon,
			String shape_pt_sequence) {
		super();
		this.shape_id = shape_id;
		this.shape_pt_lat = shape_pt_lat;
		this.shape_pt_lon = shape_pt_lon;
		this.shape_pt_sequence = shape_pt_sequence;
	}
	public String getShape_id() {
		return shape_id;
	}
	public void setShape_id(String shape_id) {
		this.shape_id = shape_id;
	}
	public String getShape_pt_lat() {
		return shape_pt_lat;
	}
	public void setShape_pt_lat(String shape_pt_lat) {
		this.shape_pt_lat = shape_pt_lat;
	}
	public String getShape_pt_lon() {
		return shape_pt_lon;
	}
	public void setShape_pt_lon(String shape_pt_lon) {
		this.shape_pt_lon = shape_pt_lon;
	}
	public String getShape_pt_sequence() {
		return shape_pt_sequence;
	}
	public void setShape_pt_sequence(String shape_pt_sequence) {
		this.shape_pt_sequence = shape_pt_sequence;
	}





}
