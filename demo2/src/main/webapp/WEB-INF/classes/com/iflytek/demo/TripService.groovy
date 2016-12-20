package com.iflytek.demo

import com.iflytek.test.Test

public class TripService {
	def test;
	
	public void setTest(Test test) {
		this.test = test
	}
	
	public Test getTest() {
		return test
	}
	
	def execute(int i) {
		return test.execute(i);
	}
}