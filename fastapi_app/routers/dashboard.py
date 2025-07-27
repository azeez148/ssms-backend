from fastapi import APIRouter, Depends
from sqlalchemy.orm import Session
from .. import crud, schemas
from ..database import get_db

router = APIRouter()

@router.get("/dashboard/all", response_model=schemas.Dashboard)
def get_dashboard_data(db: Session = Depends(get_db)):
    return crud.get_dashboard_data(db)
