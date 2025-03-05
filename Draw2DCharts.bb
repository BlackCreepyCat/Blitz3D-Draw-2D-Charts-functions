; ----------------------------------------
; Name : Draw 2D Charts functions
; Date : (C)2025 
; Site : https://github.com/BlackCreepyCat
; ----------------------------------------


; Define a Type for individual values
Type ChartValue
    Field value#          ; The value of the bar
    Field parent.Chart    ; Link to the parent Chart
    Field nextValue.ChartValue ; Link to the next ChartValue (linked list)
End Type

; Define a Type for the chart itself
Type Chart
    Field name$           ; Name of the chart
    Field firstValue.ChartValue ; Pointer to the first ChartValue
End Type

; Function to add a value to a Chart
Function AddChartValue(c.Chart, value#)
    Local cv.ChartValue = New ChartValue
    cv\value = value
    cv\parent = c
    
    ; If this is the first value, set it as firstValue
    If c\firstValue = Null Then
        c\firstValue = cv
    Else
        ; Find the last value and link the new one
        Local lastValue.ChartValue = c\firstValue
        While lastValue\nextValue <> Null
            lastValue = lastValue\nextValue
        Wend
        lastValue\nextValue = cv
    End If
End Function

; Function to draw a bar chart
Function DrawBarChart(c.Chart, x%, y%, width%, height%)
    ; c: Chart instance to draw
    ; x, y: top-left position of the chart
    ; width, height: dimensions of the chart
	
    ; Count the number of bars
    Local numBars% = 0
    Local cv.ChartValue = c\firstValue
    While cv <> Null
        numBars = numBars + 1
        cv = cv\nextValue
    Wend
    
    If numBars = 0 Then Return ; Exit if there’s no data
	
    Local barWidth% = width / numBars ; Width of each bar
    Local maxValue# = 0
	
    ; Find the maximum value for normalization
    cv = c\firstValue
    While cv <> Null
        If cv\value > maxValue Then maxValue = cv\value
        cv = cv\nextValue
    Wend
	
    ; Draw each bar
    Local i% = 0
    cv = c\firstValue
    While cv <> Null
        Local barHeight% = (cv\value / maxValue) * height
        Local barX% = x + i * barWidth
        Local barY% = y + height - barHeight
		
        Color 0, 255, 0 ; Green color for the bar
        Rect barX, barY, barWidth - 2, barHeight, 1 ; -2 for spacing between bars
		
        Color 255, 255, 255 ; White color for text
        Text barX + barWidth / 2, y + height + 5, Str(cv\value), True ; Label with value
		
        i = i + 1
        cv = cv\nextValue
    Wend
	
    ; Draw the axes
    Color 255, 255, 255 ; White color for axes
    Line x, y, x, y + height ; Y-axis
    Line x, y + height, x + width, y + height ; X-axis
	
    ; Display the chart name
    If c\name <> "" Then
        Text x + width / 2, y - 20, c\name, True ; Centered above the chart
    End If
End Function

; Example usage
Graphics 1024, 768, 0, 2 ; Windowed mode
SetBuffer BackBuffer()

; Create a chart
Local myChart.Chart = New Chart
myChart\name = "My Chart"

; Create a chart
Local myChartB.Chart = New Chart
myChartB\name = "My Chart B"



; Add values to the chart
AddChartValue(myChart, 10)
AddChartValue(myChart, 500)
AddChartValue(myChart, 30)
AddChartValue(myChart, 70)
AddChartValue(myChart, 200)

; Add values to the chart
AddChartValue(myChartB, 100)
AddChartValue(myChartB, 500)
AddChartValue(myChartB, 30)
AddChartValue(myChartB, 170)
AddChartValue(myChartB, 200)

Repeat
    Cls
    DrawBarChart(myChart, 100, 100, 300, 200)
	
    DrawBarChart(myChartB, 400, 300, 300, 200)
	
	
	If MouseHit(1) Then AddChartValue(myChartB, Rnd(200,4200))
	
    Flip
Until KeyHit(1) ; Exit with Esc key

; Clean up instances
Local cv.ChartValue = myChart\firstValue
While cv <> Null
    Local nextCv.ChartValue = cv\nextValue
    Delete cv
    cv = nextCv
Wend
Delete myChart

End
;~IDEal Editor Parameters:
;~C#Blitz3D