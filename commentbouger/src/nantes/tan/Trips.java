package nantes.tan;

public class Trips {

	public String route_id;
	public String service_id;
	public String trip_id;
	public String trip_headsign;
	public String direction_id;
	public String block_id;
	public String shape_id;

	public Trips(String route_id, String service_id, String trip_id,
			String trip_headsign, String direction_id, String block_id,
			String shape_id) {
		super();
		this.route_id = route_id;
		this.service_id = service_id;
		this.trip_id = trip_id;
		this.trip_headsign = trip_headsign;
		this.direction_id = direction_id;
		this.block_id = block_id;
		this.shape_id = shape_id;
	}

	public Trips() {
		super();
	}

	public String getRoute_id() {
		return route_id;
	}
	public void setRoute_id(String route_id) {
		this.route_id = route_id;
	}
	public String getService_id() {
		return service_id;
	}
	public void setService_id(String service_id) {
		this.service_id = service_id;
	}
	public String getTrip_id() {
		return trip_id;
	}
	public void setTrip_id(String trip_id) {
		this.trip_id = trip_id;
	}
	public String getTrip_headsign() {
		return trip_headsign;
	}
	public void setTrip_headsign(String trip_headsign) {
		this.trip_headsign = trip_headsign;
	}
	public String getDirection_id() {
		return direction_id;
	}
	public void setDirection_id(String direction_id) {
		this.direction_id = direction_id;
	}
	public String getBlock_id() {
		return block_id;
	}
	public void setBlock_id(String block_id) {
		this.block_id = block_id;
	}
	public String getShape_id() {
		return shape_id;
	}
	public void setShape_id(String shape_id) {
		this.shape_id = shape_id;
	}



}
