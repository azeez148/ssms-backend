from sqlalchemy import Column, Integer, String, Boolean, Float, ForeignKey, DateTime
from sqlalchemy.orm import relationship
from sqlalchemy.dialects.postgresql import JSONB
from .database import Base
import datetime

class Category(Base):
    __tablename__ = "categories"

    id = Column(Integer, primary_key=True, index=True)
    name = Column(String, index=True)
    description = Column(String)

    products = relationship("Product", back_populates="category")

class Product(Base):
    __tablename__ = "products"

    id = Column(Integer, primary_key=True, index=True)
    name = Column(String, index=True)
    description = Column(String)
    unit_price = Column(Float)
    selling_price = Column(Float)
    is_active = Column(Boolean, default=False)
    can_listed = Column(Boolean, default=False)
    size_map = Column(JSONB)

    category_id = Column(Integer, ForeignKey("categories.id"))
    category = relationship("Category", back_populates="products")

class PaymentType(Base):
    __tablename__ = "payment_types"

    id = Column(Integer, primary_key=True, index=True)
    name = Column(String, index=True)
    description = Column(String)

class DeliveryType(Base):
    __tablename__ = "delivery_types"

    id = Column(Integer, primary_key=True, index=True)
    name = Column(String, index=True)
    description = Column(String)

class Sale(Base):
    __tablename__ = "sales"

    id = Column(Integer, primary_key=True, index=True)
    customer_name = Column(String)
    customer_address = Column(String)
    customer_mobile = Column(String)
    customer_email = Column(String)
    date = Column(DateTime, default=datetime.datetime.utcnow)
    total_quantity = Column(Integer)
    total_price = Column(Float)
    payment_reference_number = Column(String)

    payment_type_id = Column(Integer, ForeignKey("payment_types.id"))
    payment_type = relationship("PaymentType")

    delivery_type_id = Column(Integer, ForeignKey("delivery_types.id"))
    delivery_type = relationship("DeliveryType")

    sale_items = relationship("SaleItem", back_populates="sale")

class SaleItem(Base):
    __tablename__ = "sale_items"

    id = Column(Integer, primary_key=True, index=True)
    product_id = Column(Integer)
    product_name = Column(String)
    product_category = Column(String)
    size = Column(String)
    quantity_available = Column(Integer)
    quantity = Column(Integer)
    sale_price = Column(Float)
    total_price = Column(Float)

    sale_id = Column(Integer, ForeignKey("sales.id"))
    sale = relationship("Sale", back_populates="sale_items")

class Purchase(Base):
    __tablename__ = "purchases"

    id = Column(Integer, primary_key=True, index=True)
    supplier_name = Column(String)
    supplier_address = Column(String)
    supplier_mobile = Column(String)
    supplier_email = Column(String)
    date = Column(DateTime, default=datetime.datetime.utcnow)
    total_quantity = Column(Integer)
    total_price = Column(Float)
    payment_reference_number = Column(String)

    payment_type_id = Column(Integer, ForeignKey("payment_types.id"))
    payment_type = relationship("PaymentType")

    delivery_type_id = Column(Integer, ForeignKey("delivery_types.id"))
    delivery_type = relationship("DeliveryType")

    purchase_items = relationship("PurchaseItem", back_populates="purchase")

class PurchaseItem(Base):
    __tablename__ = "purchase_items"

    id = Column(Integer, primary_key=True, index=True)
    product_id = Column(Integer)
    product_name = Column(String)
    product_category = Column(String)
    size = Column(String)
    quantity_available = Column(Integer)
    quantity = Column(Integer)
    purchase_price = Column(Float)
    total_price = Column(Float)

    purchase_id = Column(Integer, ForeignKey("purchases.id"))
    purchase = relationship("Purchase", back_populates="purchase_items")

class Attribute(Base):
    __tablename__ = "attributes"

    id = Column(Integer, primary_key=True, index=True)
    name = Column(String, index=True)
    description = Column(String)
    value = Column(String)

    category_id = Column(Integer, ForeignKey("categories.id"))
    category = relationship("Category")
