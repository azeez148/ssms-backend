from fastapi import APIRouter, Depends, HTTPException
from sqlalchemy.orm import Session
from typing import List
from .. import crud, models, schemas
from ..database import get_db

router = APIRouter()

@router.post("/purchases/", response_model=schemas.Purchase)
def create_purchase(purchase: schemas.PurchaseCreate, db: Session = Depends(get_db)):
    return crud.create_purchase(db=db, purchase=purchase)

@router.get("/purchases/", response_model=List[schemas.Purchase])
def read_purchases(skip: int = 0, limit: int = 100, db: Session = Depends(get_db)):
    purchases = crud.get_purchases(db, skip=skip, limit=limit)
    return purchases

@router.get("/purchases/{purchase_id}", response_model=schemas.Purchase)
def read_purchase(purchase_id: int, db: Session = Depends(get_db)):
    db_purchase = crud.get_purchase(db, purchase_id=purchase_id)
    if db_purchase is None:
        raise HTTPException(status_code=404, detail="Purchase not found")
    return db_purchase
