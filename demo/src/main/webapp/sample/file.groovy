import java.io.*

def up = $request.parameters.get('up')

if (up == 'true') {
	$response.println "Summary: ${$request.parameters.summary} <p/>"
	$request.body.each {key,file->
		$response.println "Empty: ${file.empty}, Name: ${file.name}, File Name: ${file.originalFilename}, "
		$response.println "Content Type: ${file.contentType}, Size: ${file.size} <br/>"
		//file.inputStream
		//file.bytes

		//save file		
		File f = new File("/tmp/${file.originalFilename}")
		if (f.exists()) {
			f.delete()
		}
		file.transferTo f
		$response.println "Save to: ${f.path} <br/>"
	}
} else {
	$response.println '''
	<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 3.2 Final//EN">
	<html>
	 <head>
	  <title>Test upload file</title>
	 </head>
	 <body>
	  <form action="file.groovy" method="post" enctype="multipart/form-data">
	   <input type="hidden" name="up" value="true"/>
	   Summary: <input type="text" name="summary"/><br/>
	   File: <input type="file" name="file1"/><br/>
	   File: <input type="file" name="file2"/><br/>
	   <input type="submit" value="upload"/>
	  </form>
	 </body>
	</html>
	'''
}
