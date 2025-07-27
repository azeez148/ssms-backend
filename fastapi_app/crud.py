from sqlalchemy.orm import Session
from . import models, schemas

# Category CRUD operations
def get_category(db: Session, category_id: int):
    return db.query(models.Category).filter(models.Category.id == category_id).first()

def get_category_by_name(db: Session, name: str):
    return db.query(models.Category).filter(models.Category.name == name).first()

def get_categories(db: Session, skip: int = 0, limit: int = 100):
    return db.query(models.Category).offset(skip).limit(limit).all()

def create_category(db: Session, category: schemas.CategoryCreate):
    db_category = models.Category(name=category.name, description=category.description)
    db.add(db_category)
    db.commit()
    db.refresh(db_category)
    return db_category

# Product CRUD operations
def get_product(db: Session, product_id: int):
    return db.query(models.Product).filter(models.Product.id == product_id).first()

def get_products(db: Session, skip: int = 0, limit: int = 100):
    return db.query(models.Product).offset(skip).limit(limit).all()

def create_product(db: Session, product: schemas.ProductCreate):
    db_product = models.Product(**product.dict())
    db.add(db_product)
    db.commit()
    db.refresh(db_product)
    return db_product

def update_product(db: Session, product_id: int, product: schemas.ProductUpdate):
    db_product = get_product(db, product_id)
    if db_product:
        update_data = product.dict(exclude_unset=True)
        for key, value in update_data.items():
            setattr(db_product, key, value)
        db.commit()
        db.refresh(db_product)
    return db_product

def delete_product(db: Session, product_id: int):
    db_product = get_product(db, product_id)
    if db_product:
        db.delete(db_product)
        db.commit()
    return db_product

# PaymentType CRUD operations
def get_payment_type(db: Session, payment_type_id: int):
    return db.query(models.PaymentType).filter(models.PaymentType.id == payment_type_id).first()

def get_payment_types(db: Session, skip: int = 0, limit: int = 100):
    return db.query(models.PaymentType).offset(skip).limit(limit).all()

def create_payment_type(db: Session, payment_type: schemas.PaymentTypeCreate):
    db_payment_type = models.PaymentType(**payment_type.dict())
    db.add(db_payment_type)
    db.commit()
    db.refresh(db_payment_type)
    return db_payment_type

# DeliveryType CRUD operations
def get_delivery_type(db: Session, delivery_type_id: int):
    return db.query(models.DeliveryType).filter(models.DeliveryType.id == delivery_type_id).first()

def get_delivery_types(db: Session, skip: int = 0, limit: int = 100):
    return db.query(models.DeliveryType).offset(skip).limit(limit).all()

def create_delivery_type(db: Session, delivery_type: schemas.DeliveryTypeCreate):
    db_delivery_type = models.DeliveryType(**delivery_type.dict())
    db.add(db_delivery_type)
    db.commit()
    db.refresh(db_delivery_type)
    return db_delivery_type

# Sale CRUD operations
def get_sale(db: Session, sale_id: int):
    return db.query(models.Sale).filter(models.Sale.id == sale_id).first()

def get_sales(db: Session, skip: int = 0, limit: int = 100):
    return db.query(models.Sale).offset(skip).limit(limit).all()

def create_sale(db: Session, sale: schemas.SaleCreate):
    db_sale = models.Sale(**sale.dict(exclude={'sale_items'}))
    for item in sale.sale_items:
        db_item = models.SaleItem(**item.dict())
        db_sale.sale_items.append(db_item)
    db.add(db_sale)
    db.commit()
    db.refresh(db_sale)
    return db_sale
