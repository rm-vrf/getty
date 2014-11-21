import cn.batchfile.demo.service.*

def tripService = new TripService()
def trip = tripService.getTrip('1001')
$response.println trip
