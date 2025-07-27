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

# Purchase CRUD operations
def get_purchase(db: Session, purchase_id: int):
    return db.query(models.Purchase).filter(models.Purchase.id == purchase_id).first()

def get_purchases(db: Session, skip: int = 0, limit: int = 100):
    return db.query(models.Purchase).offset(skip).limit(limit).all()

def create_purchase(db: Session, purchase: schemas.PurchaseCreate):
    db_purchase = models.Purchase(**purchase.dict(exclude={'purchase_items'}))
    for item in purchase.purchase_items:
        db_item = models.PurchaseItem(**item.dict())
        db_purchase.purchase_items.append(db_item)
    db.add(db_purchase)
    db.commit()
    db.refresh(db_purchase)
    return db_purchase

# Dashboard logic
def get_dashboard_data(db: Session):
    recent_sales = db.query(models.Sale).order_by(models.Sale.date.desc()).limit(5).all()
    recent_purchases = db.query(models.Purchase).order_by(models.Purchase.date.desc()).limit(5).all()

    # This is a placeholder for most sold items.
    # A more complex query would be needed to get the actual most sold items.
    most_sold_items = {}

    total_sales = db.query(models.Sale).count()
    total_revenue = db.query(models.Sale).with_entities(models.Sale.total_price).all()
    total_revenue = sum([r[0] for r in total_revenue])

    total_purchases = db.query(models.Purchase).count()
    total_cost = db.query(models.Purchase).with_entities(models.Purchase.total_price).all()
    total_cost = sum([c[0] for c in total_cost])

    products = db.query(models.Product).all()
    total_items_in_stock = 0
    total_stock_value = 0
    category_item_counts = {}

    for product in products:
        if product.size_map:
            quantity = sum(product.size_map.values())
            total_items_in_stock += quantity
            total_stock_value += quantity * product.unit_price
            if product.category.name in category_item_counts:
                category_item_counts[product.category.name] += quantity
            else:
                category_item_counts[product.category.name] = quantity


    dashboard = schemas.Dashboard(
        recent_sales=recent_sales,
        recent_purchases=recent_purchases,
        most_sold_items=most_sold_items,
        total_sales=total_sales,
        total_revenue=total_revenue,
        total_purchases=total_purchases,
        total_cost=total_cost,
        total_items_in_stock=total_items_in_stock,
        total_stock_value=total_stock_value,
        category_item_counts=category_item_counts,
    )
    return dashboard

# Home logic
def get_home_data(db: Session):
    products = get_products(db, limit=100) # limiting to 100 products for now
    home = schemas.Home(products=products)
    return home
