package cn.batchfile.demo.service

class TripService {
	
	def getTrip(id) {
		println 'get trip info: ' + id
		return ['id': id, 'location': 'Phuket']
	}
	
	def putTrip(trip) {
		println 'put trip: ' + trip
	}
	
	def deleteTrip(id) {
		println 'delete trip: ' + id 
	}
}
