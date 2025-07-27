from fastapi import FastAPI, BackgroundTasks
from pydantic import BaseModel
import pywhatkit as kit
import datetime
import redis
import json
import time
from mangum import Mangum

app = FastAPI()
handler = Mangum(app)

# Placeholder for Redis connection
r = redis.Redis(host='localhost', port=6379, decode_responses=True)

class Message(BaseModel):
    phone: str
    message: str

def send_whatsapp_message(phone: str, message: str):
    """
    Sends a WhatsApp message using pywhatkit.
    """
    try:
        now = datetime.datetime.now()
        kit.sendwhatmsg_instantly(phone, message, wait_time=15)
        print(f"✅ Sent to {phone}: {message}")
    except Exception as e:
        print(f"❌ Error sending message to {phone}: {e}")

@app.post("/send")
async def send(message: Message, background_tasks: BackgroundTasks):
    """
    Adds a message to the background tasks to be sent.
    """
    background_tasks.add_task(send_whatsapp_message, message.phone, message.message)
    return {"status": "Message sending scheduled"}

@app.get("/")
def read_root():
    return {"Hello": "World"}
