;Assignment 2 CPSC 359
;Tom Crowfoot 10037477 T04, Kyle Kajorinne 10044877 T02

%define XGAMODE	4105h					;XGA video mode code
%define XGAXRES 1024					;horizontal resolution
%define XGAYRES	768   					;vertical resolution
%define	SPACE 106						;space in maze
%define	BOXXRES 200						;x resolution for box in maze
%define	BOXYRES	225						;y resolution for box in maze

%macro setupXGA 0						;saves old video mode and sets new mode to xga
         mov  ah,0fh        			;get video mode
         int  10h              
         mov  [vidmode],al  			;save video mode
         mov  eax,4f02h     			;set SVGA video mode
         mov  ebx,XGAMODE   			;select XGA video mode
         int  10h                    
         mov  ax,fs         			;frame buffer into es
         mov  es,ax            
%endmacro

%macro wait 0							;waits for keypress
         push eax
         mov  ah,1
         int  0f1h
         pop eax
%endmacro

%macro cleanXGA 0						;goes back to previous video mode
         mov  ah,00h
         mov  al,[vidmode]
         int  10h
%endmacro

								
[EXTERN _randomNumber]					;c functions to get random int
[GLOBAL mystart]

[SECTION .text]
mystart:
	setupXGA
	call	drawBackground
starting:
	
	push	dword 10					;y
	push	dword 10					;x
	call	drawPacman
	call 	clearFood
	call 	randomFood
	
	push	dword 66					;y
	push	dword 66					;x
	call	drawTrolls
	;wait
	call 	waitVRetrace
	call	moveTrolls
	jmp		starting
end:
	cleanXGA
    ret
	
	
clearFood:							;clear the food positions
	enter 0,0
	push	dword 15				;colour white
	push	dword 40				;width
	push	dword 40				;length
	push	dword 10				;y
	push	dword 100				;x
	call	drawRect					
	push	dword 15				;colour white
	push	dword 40				;width
	push	dword 40				;length
	push	dword 10				;y
	push	dword 200				;x
	call	drawRect	
	push	dword 15				;colour white
	push	dword 40				;width
	push	dword 40				;length
	push	dword 10				;y
	push	dword 300				;x
	call	drawRect	
	push	dword 15				;colour white
	push	dword 40				;width
	push	dword 40				;length
	push	dword 10				;y
	push	dword 400				;x
	call	drawRect	
	push	dword 15				;colour white
	push	dword 40				;width
	push	dword 40				;length
	push	dword 10				;y
	push	dword 500				;x
	call	drawRect	
	push	dword 15				;colour white
	push	dword 40				;width
	push	dword 40				;length
	push	dword 10				;y
	push	dword 600				;x
	call	drawRect	
	push	dword 15				;colour white
	push	dword 40				;width
	push	dword 40				;length
	push	dword 10				;y
	push	dword 700				;x
	call	drawRect	
	

	leave
	ret
	
waitVRetrace:						; wait for the writing phase
        push    ebp
        mov     ebp, esp

        pushad

        ;input port in dx register
        mov     dx, 0x3DA

.loop1:
        ;read from port 0x3DA
        in      eax, dx

        ;keep looping while 4th bit is set
        and     eax, 0x08
        cmp     eax, 0
        jne     .loop1

.loop2:
        ;read byte from port
        in      eax, dx

        ;keep looping while 4th bit is clear
        and     eax, 0x08
        cmp     eax, 0
        je      .loop2

        popad

        mov     esp, ebp
        pop     ebp

        ret
		
moveTrolls:								;moves the trolls around their squares, starting position for main troll is 66,66
	enter	8,0							;no args
	mov		dword[ebp-8],66				;local y starting position for trolls
	mov		dword[ebp-4],66				;local x starting position for trolls
startMoveTroll:
	mov		ecx,250
moveTrollRight:
	inc		dword[ebp-4]				;x++
	push	dword[ebp-8]				;y
	push	dword[ebp-4]				;x
	call 	waitVRetrace
	call	drawTrolls					;draw the moved trolls
	loop	moveTrollRight
	mov		ecx,275
moveTrollDown:
	inc		dword[ebp-8]				;y++
	push	dword[ebp-8]				;y
	push	dword[ebp-4]				;x

	call 	waitVRetrace
	call	drawTrolls					;draw the moved trolls
	loop	moveTrollDown
	mov		ecx,250
moveTrollLeft:
	dec		dword[ebp-4]				;x--
	push	dword[ebp-8]				;y
	push	dword[ebp-4]				;x
	call 	waitVRetrace
	call	drawTrolls					;draw the moved trolls
	loop	moveTrollLeft
	mov		ecx,275
moveTrollUp:
	dec		dword[ebp-8]				;y--
	push	dword[ebp-8]				;y
	push	dword[ebp-4]				;x
	call 	waitVRetrace
	call	drawTrolls					;draw the moved trolls
	loop	moveTrollUp
	leave
	ret
		
drawTrolls:								;draws all 6 trolls with other 5 relation to the upper left troll
	enter	0,0							;args((+8)x,(+12)y)
	push	dword[ebp+12]				;y
	push	dword[ebp+8]				;x
	call	drawTroll					;draw main troll
	add		dword[ebp+8],306			;x offset
	push	dword[ebp+12]				;y
	push	dword[ebp+8]				;x
	call	drawTroll					;draw troll 2
	add		dword[ebp+8],306			;x offset
	push	dword[ebp+12]				;y
	push	dword[ebp+8]				;x
	call	drawTroll					;draw troll 3
	add		dword[ebp+12],331			;y offset
	add		dword[ebp+8],-612			;reset x offset
	push	dword[ebp+12]				;y
	push	dword[ebp+8]				;x
	call	drawTroll					;draw troll 4
	add		dword[ebp+8],306			;x offset
	push	dword[ebp+12]				;y
	push	dword[ebp+8]				;x
	call	drawTroll					;draw troll 5
	add		dword[ebp+8],306			;x offset
	push	dword[ebp+12]				;y
	push	dword[ebp+8]				;x
	call	drawTroll					;draw troll 6
	leave
	ret		8
	
drawBackground:							;draws background as white with 6 blue rectangles
	enter	0,0							;no args
	
	push	dword 15					;colour white
	push	dword XGAXRES-1				;width
	push	dword XGAYRES-1				;length
	push	dword 0						;y
	push	dword 0						;x
	
	call	drawRect					;draw the background
	push	dword SPACE					;y rectangle1
	push	dword SPACE					;x

	call	drawBackgroundRect
	push	dword SPACE					;y rectangle2
	push	dword 2*SPACE+BOXXRES		;x
	
	call	drawBackgroundRect
	push	dword SPACE					;y rectangle3
	push	dword 3*SPACE+2*BOXXRES		;x
	
	call	drawBackgroundRect
	push	dword 2*SPACE+BOXYRES		;y rectangle4
	push	dword SPACE					;x
	
	call	drawBackgroundRect
	push	dword 2*SPACE+BOXYRES		;y rectangle5
	push	dword 2*SPACE+BOXXRES		;x
	
	call	drawBackgroundRect
	push	dword 2*SPACE+BOXYRES		;y rectangle6
	push	dword 3*SPACE+2*BOXXRES		;x

	call	drawBackgroundRect
	leave
 	ret
	
drawBackgroundRect:						;draws the 6 blue rectangles
	enter	0,0							;args((+8)x,(+12)y)
	push	dword 1						;colour BLUE
	push	dword BOXXRES				;width
	push	dword BOXYRES				;length
	push	dword[ebp+12]				;y
	push	dword[ebp+8]				;x
	call 	waitVRetrace
	call	drawRect					;draw the background
	leave
	ret	8			
							
drawPacman:								;draws pacman character with top left corner at (x,y)
	enter	0,0							;args((+8)x,(+12)y)
	push	dword 44 					;colour YELLOW
	push	dword 30					;width
	push	dword 30					;length
	push	dword[ebp+12]				;y
	push	dword[ebp+8]				;x
	call	drawRect					;draw body
	push	dword 0						;colour BLACK
	push	dword 5						;width
	push	dword 5						;length
	add		dword[ebp+12],7				;y offset eye
	add		dword[ebp+8],7				;x offset eye
	push	dword[ebp+12]				;y
	push	dword[ebp+8]				;x
	call	drawRect					;draw eye feature
	push	dword 0						;colour BLACK
	push	dword 15					;width
	push	dword 7						;length
	add		dword[ebp+12],10			;y offset mouth
	add		dword[ebp+8],8				;x offset mouth
	push	dword[ebp+12]				;y
	push	dword[ebp+8]				;x
	call	drawRect					;draw mouth feature
	leave
 	ret	8
							
drawFood:								;draws a food item with top left corner at (x,y)
	enter	0,0							;args((+8)x,(+12)y)
	push	dword 4 					;colour RED
	push	dword 15					;width
	push	dword 15					;length
	push	dword[ebp+12]				;y
	push	dword[ebp+8]				;x
	call	drawRect					;draw one part
	push	dword 4 					;colour RED
	push	dword 15					;width
	push	dword 15					;length
	add		dword[ebp+12],-2			;y offset
	add		dword[ebp+8],16				;x offset
	push	dword[ebp+12]				;y
	push	dword[ebp+8]				;x
	call	drawRect					;draw two part
	push	dword 2 					;colour GREEN
	push	dword 9						;width
	push	dword 9						;length
	add		dword[ebp+12],-3			;y offset
	add		dword[ebp+8],-5				;x offset
	push	dword[ebp+12]				;y
	push	dword[ebp+8]				;x
	call	drawRect					;draw three part
	leave
	ret	8
							
drawTroll:								;draws a troll with top left corner at (x,y)
	enter	0,0							;args((+8)x,(+12)y)
	push	dword 15 					;colour white
	push	dword 32					;width
	push	dword 32					;length
	push	dword[ebp+12]				;y
	push	dword[ebp+8]				;x
	call	drawRect
	push	dword 2 					;colour GREEN
	push	dword 30					;width
	push	dword 30					;length
	add		dword[ebp+12],1				;y offset eye
	add		dword[ebp+8],1				;x offset eye
	push	dword[ebp+12]				;y
	push	dword[ebp+8]				;x
	call	drawRect					;draw body
	push	dword 0						;colour BLACK
	push	dword 5						;width
	push	dword 5						;length
	add		dword[ebp+12],8				;y offset eye
	add		dword[ebp+8],8				;x offset eye
	push	dword[ebp+12]				;y
	push	dword[ebp+8]				;x
	call	drawRect					;draw eye feature
	push	dword 0						;colour BLACK
	push	dword 5						;width
	push	dword 5						;length
	add		dword[ebp+8],14				;x offset eye2
	push	dword[ebp+12]				;y
	push	dword[ebp+8]				;x
	call	drawRect					;draw eye2 feature
	push	dword 0						;colour BLACK
	push	dword 10					;width
	push	dword 8						;length
	add		dword[ebp+12],11			;y offset mouth
	add		dword[ebp+8],-7				;x offset mouth
	push	dword[ebp+12]				;y
	push	dword[ebp+8]				;x
	call	drawRect					;draw mouth feature
	leave
	ret	8
							
drawRect:								;draws a rectangle with topleft corner at (x,y) of specified length/width/solid colour
    enter 0,0							;args((+8)x,(+12)y,(+16)length,(+20)width,(+24)colour)
    push dword eax						;push used regesters
	push dword ebx
	push dword ecx
	push dword edx
    mov eax,[ebp+12]					;calculate rectangle offset=(y*width)+x
    mov ebx,XGAXRES
    mul ebx
    add eax,[ebp+8]						;eax=first pixel of rectangle
    mov ebx,0          					;ebx counts number of rows
init:
    mov ecx,[ebp+20]					;ecx counts number of pixels in line
draw:                           
    mov dl,[ebp+24]						;draw one horiz line
    mov byte[es:eax],dl					;colour pixel to set colour
    inc eax								;next pixel
    loop draw							;repeat for whole line
    sub eax,[ebp+20]   					;move cursor back to begining of rect
    add eax, XGAXRES   					;move one row down
    inc ebx
    cmp ebx,[ebp+16]					;check if all rows drawn(ebx=length)
    jle init           					;jump for ebx<length
	pop dword edx						;pop used registers
	pop dword ecx
	pop dword ebx
	pop dword eax
    leave
    ret 20

randomFood:								;places the food at a random preset location
	enter 0,0
	pushad
	
	call    _randomNumber				;calls the external c code for a random number between 0-6
	cmp 	eax, 0
	je		posZero
	cmp		eax, 1
	je		posOne
	cmp		eax, 2
	je		posTwo
	cmp		eax, 3
	je		posThree
	cmp		eax, 4
	je		posFour
	cmp 	eax, 5
	je		posFive
	jmp		posSix
	
posZero:
	push	dword 15					;y
	push	dword 100					;x
	call	drawFood
	jmp		endFood
	posOne:
	push	dword 15					;y
	push	dword 200					;x
	call	drawFood
	jmp		endFood
	posTwo:
	push	dword 15					;y
	push	dword 300					;x
	call	drawFood
	jmp		endFood
	posThree:
	push	dword 15					;y
	push	dword 400					;x
	call	drawFood
	jmp		endFood
	posFour:
	push	dword 15					;y
	push	dword 500					;x
	call	drawFood
	jmp		endFood
	posFive:
	push	dword 15					;y
	push	dword 600					;x
	call	drawFood
	jmp		endFood
	posSix:
	push	dword 15					;y
	push	dword 700					;x
	call	drawFood
endFood:
	leave
	ret

[SECTION .data]
[SECTION .bss]
vidmode	 resb	1						;old video mode