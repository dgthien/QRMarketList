package com.qrmarketlist.core;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import net.glxn.qrgen.QRCode;
import net.glxn.qrgen.image.ImageType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.qrmarketlist.core.product.Product;

public class QRCodeUtil {

	private static Logger logger = LoggerFactory.getLogger(QRCodeUtil.class);

	//TODO: Verificar com o Rafa como vamos fazer a parte da configuração, se o caminho será salvo em configuração ou terá FileChooser para o usuário...
	public static String printQRCode(Product product, String path) {
		path = "/home/greengo/Documentos/";
		String fileName = path+product.getId()+"-"+product.getName()+".PNG";

		StringBuffer details = new StringBuffer();
		details.append("Name: "  + product.getName() + "\n");
		details.append("Description: " + product.getDescription()+ "\n");
		details.append("Price: " + product.getPrice());
		details.trimToSize();
		System.out.println(details.toString());
		
		ByteArrayOutputStream out = QRCode.from(details.toString()).to(ImageType.PNG).stream();
		try {
			FileOutputStream fout = new FileOutputStream(new File(fileName));
			fout.write(out.toByteArray());
			fout.flush();
			fout.close();
			return fileName;
		} catch (IOException e) {
			logger.debug("Error while creating QRCode of product "+product.getName());
		}
		return null;
	}
}