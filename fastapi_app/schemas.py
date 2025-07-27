from pydantic import BaseModel
from typing import Optional, Dict, List
import datetime

class CategoryBase(BaseModel):
    name: str
    description: Optional[str] = None

class CategoryCreate(CategoryBase):
    pass

class Category(CategoryBase):
    id: int

    class Config:
        orm_mode = True

class Home(BaseModel):
    products: List[Product]

    class Config:
        orm_mode = True

class Dashboard(BaseModel):
    recent_sales: List[Sale]
    recent_purchases: List[Purchase]
    most_sold_items: Dict[str, int]
    total_sales: int
    total_revenue: float
    total_purchases: int
    total_cost: float
    total_items_in_stock: int
    total_stock_value: float
    category_item_counts: Dict[str, int]

    class Config:
        orm_mode = True

class PurchaseItemBase(BaseModel):
    product_id: int
    product_name: str
    product_category: str
    size: str
    quantity_available: int
    quantity: int
    purchase_price: float
    total_price: float

class PurchaseItemCreate(PurchaseItemBase):
    pass

class PurchaseItem(PurchaseItemBase):
    id: int

    class Config:
        orm_mode = True

class PurchaseBase(BaseModel):
    supplier_name: str
    supplier_address: str
    supplier_mobile: str
    supplier_email: str
    date: datetime.datetime
    total_quantity: int
    total_price: float
    payment_reference_number: str

class PurchaseCreate(PurchaseBase):
    payment_type_id: int
    delivery_type_id: int
    purchase_items: List[PurchaseItemCreate]

class Purchase(PurchaseBase):
    id: int
    payment_type: PaymentType
    delivery_type: DeliveryType
    purchase_items: List[PurchaseItem] = []

    class Config:
        orm_mode = True

class PaymentTypeBase(BaseModel):
    name: str
    description: Optional[str] = None

class PaymentTypeCreate(PaymentTypeBase):
    pass

class PaymentType(PaymentTypeBase):
    id: int

    class Config:
        orm_mode = True

class DeliveryTypeBase(BaseModel):
    name: str
    description: Optional[str] = None

class DeliveryTypeCreate(DeliveryTypeBase):
    pass

class DeliveryType(DeliveryTypeBase):
    id: int

    class Config:
        orm_mode = True

class SaleItemBase(BaseModel):
    product_id: int
    product_name: str
    product_category: str
    size: str
    quantity_available: int
    quantity: int
    sale_price: float
    total_price: float

class SaleItemCreate(SaleItemBase):
    pass

class SaleItem(SaleItemBase):
    id: int

    class Config:
        orm_mode = True

class SaleBase(BaseModel):
    customer_name: str
    customer_address: str
    customer_mobile: str
    customer_email: str
    date: datetime.datetime
    total_quantity: int
    total_price: float
    payment_reference_number: str

class SaleCreate(SaleBase):
    payment_type_id: int
    delivery_type_id: int
    sale_items: List[SaleItemCreate]

class Sale(SaleBase):
    id: int
    payment_type: PaymentType
    delivery_type: DeliveryType
    sale_items: List[SaleItem] = []

    class Config:
        orm_mode = True

class ProductBase(BaseModel):
    name: str
    description: Optional[str] = None
    unit_price: float
    selling_price: float
    is_active: bool = False
    can_listed: bool = False
    size_map: Optional[Dict[str, int]] = None

class ProductCreate(ProductBase):
    category_id: int

class ProductUpdate(ProductBase):
    pass

class Product(ProductBase):
    id: int
    category: Category

    class Config:
        orm_mode = True
