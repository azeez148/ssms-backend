from fastapi import APIRouter, Depends, HTTPException
from fastapi.responses import FileResponse
from sqlalchemy.orm import Session
from .. import crud, schemas
from ..database import get_db
import os

router = APIRouter()

@router.get("/public/all", response_model=schemas.Home)
def get_home_data(db: Session = Depends(get_db)):
    return crud.get_home_data(db)

@router.get("/public/{product_id}/image")
def get_product_image(product_id: int):
    image_path = f"images/products/{product_id}"
    if os.path.exists(image_path) and os.listdir(image_path):
        image_file = os.listdir(image_path)[0]
        return FileResponse(os.path.join(image_path, image_file))
    else:
        not_found_image = "images/notfound.png"
        if os.path.exists(not_found_image):
            return FileResponse(not_found_image)
        else:
            raise HTTPException(status_code=404, detail="Image not found")
