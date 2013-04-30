package com.qrmarketlist.core.product;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import com.qrmarketlist.core.AbstractTenantEntity;

@Entity
@Table(name = "PRODUCT")
@PrimaryKeyJoinColumn(name = "ID")
public class Product extends AbstractTenantEntity {

	private static final long serialVersionUID = 1L;
	private String name;
	private String description;
	private String ean;
	private ProductEnum status;
	private Double price;
	private Boolean qrCodePrinted;

	@Column(name = "NAME", length = 100, nullable = false)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "DESCRIPTION", length = 200, nullable = false)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	@Column(name = "EAN", length = 50, nullable = false)
	public String getEan() {
		return ean;
	}

	public void setEan(String ean) {
		this.ean = ean;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "STATUS", length = 50, nullable = false)
	public ProductEnum getStatus() {
		return status;
	}

	public void setStatus(ProductEnum status) {
		this.status = status;
	}

	@Column(name = "PRICE", nullable = true, columnDefinition="Decimal(10,2)")
	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	@Column(name = "QRCODE_PRINTED", nullable = true)
	public Boolean getQrCodePrinted() {
		return qrCodePrinted;
	}

	public void setQrCodePrinted(Boolean qrCodePrinted) {
		this.qrCodePrinted = qrCodePrinted;
	}
}
