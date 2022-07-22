from django.shortcuts import render
from django.http import JsonResponse, HttpResponse
from django.db import connection

def getAEDs(request):
    if request.method != 'GET':
        return HttpResponse(status=404)
    
    cursor = connection.cursor()
    cursor.execute('SELECT * FROM aeds;')
    rows = cursor.fetchall()

    response = {}
    response['AEDs'] = rows
    return JsonResponse(response)
