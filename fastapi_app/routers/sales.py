from fastapi import APIRouter, Depends, HTTPException
from sqlalchemy.orm import Session
from typing import List
from .. import crud, models, schemas
from ..database import get_db

router = APIRouter()

@router.post("/payment_types/", response_model=schemas.PaymentType)
def create_payment_type(payment_type: schemas.PaymentTypeCreate, db: Session = Depends(get_db)):
    return crud.create_payment_type(db=db, payment_type=payment_type)

@router.get("/payment_types/", response_model=List[schemas.PaymentType])
def read_payment_types(skip: int = 0, limit: int = 100, db: Session = Depends(get_db)):
    payment_types = crud.get_payment_types(db, skip=skip, limit=limit)
    return payment_types

@router.post("/sales/", response_model=schemas.Sale)
def create_sale(sale: schemas.SaleCreate, db: Session = Depends(get_db)):
    return crud.create_sale(db=db, sale=sale)

@router.get("/sales/", response_model=List[schemas.Sale])
def read_sales(skip: int = 0, limit: int = 100, db: Session = Depends(get_db)):
    sales = crud.get_sales(db, skip=skip, limit=limit)
    return sales

@router.get("/sales/{sale_id}", response_model=schemas.Sale)
def read_sale(sale_id: int, db: Session = Depends(get_db)):
    db_sale = crud.get_sale(db, sale_id=sale_id)
    if db_sale is None:
        raise HTTPException(status_code=404, detail="Sale not found")
    return db_sale
