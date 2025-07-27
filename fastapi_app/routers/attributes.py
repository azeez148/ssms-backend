from fastapi import APIRouter, Depends, HTTPException
from sqlalchemy.orm import Session
from typing import List
from .. import crud, models, schemas
from ..database import get_db
from collections import defaultdict

router = APIRouter()

@router.post("/attributes/addAttributes", response_model=List[schemas.Attribute])
def create_attributes(attributes: List[schemas.AttributeCreate], db: Session = Depends(get_db)):
    created_attributes = []
    for attribute in attributes:
        created_attributes.append(crud.create_attribute(db=db, attribute=attribute))
    return created_attributes

@router.get("/attributes/all", response_model=List[schemas.AttributeResponse])
def read_attributes(skip: int = 0, limit: int = 100, db: Session = Depends(get_db)):
    attributes = crud.get_attributes(db, skip=skip, limit=limit)
    attribute_map = defaultdict(lambda: {"description": None, "values": []})
    for attr in attributes:
        attribute_map[attr.name]["description"] = attr.description
        attribute_map[attr.name]["values"].append(attr.value)

    response = []
    for name, data in attribute_map.items():
        response.append(schemas.AttributeResponse(name=name, description=data["description"], values=data["values"]))

    return response
