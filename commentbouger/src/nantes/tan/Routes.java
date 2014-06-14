package nantes.tan;

public class Routes {

	public String route_id;
	public String route_short_name;
	public String route_long_name;
	public String route_desc;
	public String route_type;



	public Routes() {
		super();
	}



	public Routes(String route_id, String route_short_name,
			String route_long_name, String route_desc, String route_type) {
		super();
		this.route_id = route_id;
		this.route_short_name = route_short_name;
		this.route_long_name = route_long_name;
		this.route_desc = route_desc;
		this.route_type = route_type;
	}



	public String getRoute_id() {
		return route_id;
	}



	public void setRoute_id(String route_id) {
		this.route_id = route_id;
	}



	public String getRoute_short_name() {
		return route_short_name;
	}



	public void setRoute_short_name(String route_short_name) {
		this.route_short_name = route_short_name;
	}



	public String getRoute_long_name() {
		return route_long_name;
	}



	public void setRoute_long_name(String route_long_name) {
		this.route_long_name = route_long_name;
	}



	public String getRoute_desc() {
		return route_desc;
	}



	public void setRoute_desc(String route_desc) {
		this.route_desc = route_desc;
	}



	public String getRoute_type() {
		return route_type;
	}



	public void setRoute_type(String route_type) {
		this.route_type = route_type;
	}



}
