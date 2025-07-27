from fastapi import APIRouter, Depends, HTTPException
from sqlalchemy.orm import Session
from typing import List
from .. import crud, models, schemas
from ..database import get_db

router = APIRouter()

@router.post("/delivery_types/", response_model=schemas.DeliveryType)
def create_delivery_type(delivery_type: schemas.DeliveryTypeCreate, db: Session = Depends(get_db)):
    return crud.create_delivery_type(db=db, delivery_type=delivery_type)

@router.get("/delivery_types/", response_model=List[schemas.DeliveryType])
def read_delivery_types(skip: int = 0, limit: int = 100, db: Session = Depends(get_db)):
    delivery_types = crud.get_delivery_types(db, skip=skip, limit=limit)
    return delivery_types
