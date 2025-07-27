from fastapi import APIRouter, Depends, UploadFile, File, HTTPException
from sqlalchemy.orm import Session
import pandas as pd
import json
from .. import crud, schemas
from ..database import get_db

router = APIRouter()

@router.post("/stock/uploadExcel")
async def upload_excel(file: UploadFile = File(...), db: Session = Depends(get_db)):
    if not file.filename.endswith('.xlsx'):
        raise HTTPException(status_code=400, detail="Invalid file format. Please upload an .xlsx file.")

    try:
        df = pd.read_excel(file.file)
        for index, row in df.iterrows():
            category_name = row["category"]
            category = crud.get_category_by_name(db, name=category_name)
            if not category:
                category_schema = schemas.CategoryCreate(name=category_name)
                category = crud.create_category(db, category_schema)

            size_map_str = row["sizeMap"]
            # Convert to valid JSON: Add double quotes to keys
            json_string = size_map_str.replace("'", "\"")
            size_map = json.loads(json_string)

            product = schemas.ProductCreate(
                name=row["name"],
                description=row["description"],
                category_id=category.id,
                size_map=size_map,
                unit_price=row["unitPrice"],
                selling_price=row["sellingPrice"]
            )
            crud.create_product(db, product)
        return {"message": "Excel uploaded and processed successfully."}
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"Could not upload/process the file: {e}")
