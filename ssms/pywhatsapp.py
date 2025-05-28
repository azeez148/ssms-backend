import pywhatkit as kit
import redis
import json
import time
import datetime

r = redis.Redis(host='localhost', port=6379, decode_responses=True)

def send_message(phone, message):
    now = datetime.datetime.now()
    send_time = f"{now.hour}:{now.minute + 2}"  # schedule 2 min ahead
    kit.sendwhatmsg(phone, message, int(send_time.split(':')[0]), int(send_time.split(':')[1]))
    print(f"✅ Sent to {phone}: {message}")

while True:
    try:
        item = r.rpop("whatsapp:queue")
        if item:
            data = json.loads(item)
            send_message(data["phone"], data["message"])
        else:
            time.sleep(1)
    except Exception as e:
        print(f"❌ Error: {e}")
        time.sleep(2)