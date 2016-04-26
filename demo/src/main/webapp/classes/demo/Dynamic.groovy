package demo

class Dynamic {
	Entity entity
	
	def fin(int i) {
		entity = new Entity()
		entity.val = i
		return entity.val + 10
	}
}