from pydantic import BaseModel
from typing import Optional, Dict

class CategoryBase(BaseModel):
    name: str
    description: Optional[str] = None

class CategoryCreate(CategoryBase):
    pass

class Category(CategoryBase):
    id: int

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
