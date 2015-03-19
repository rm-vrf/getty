import java.io.InputStream;

import org.apache.commons.io.IOUtils;

import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileInputStream;


// fixed domain & password
String domain = ip
String username = 'administrator'
String password = 'pssword'

// get samba address from input
String uri = String.format("smb://%s/share/%s/%s", ip, date, file);
$logger.info('download file: ' + uri);

// create samba file
NtlmPasswordAuthentication auth= new NtlmPasswordAuthentication(domain, username, password);
SmbFile file = new SmbFile(uri, auth);

// write header: Content-Length
int size = file.getContentLength();
$logger.debug('size: ' + size);
$response.headers['Content-Length'] = size

// write stream
InputStream stream = null;
try {
	stream = new SmbFileInputStream(file);
	$response.write(stream)
} finally {
	IOUtils.closeQuietly(stream);
}
