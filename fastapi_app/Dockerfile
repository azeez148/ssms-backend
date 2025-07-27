FROM public.ecr.aws/lambda/python:3.9

COPY main.py requirements.txt ./

RUN python3.9 -m pip install -r requirements.txt

CMD ["main.handler"]
