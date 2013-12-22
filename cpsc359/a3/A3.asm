;Assignment 3 CPSC 359
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
[GLOBAL _myKeyInt]
[GLOBAL _myKeyInt_Size]
[GLOBAL _keyVal]

[SECTION .text]
_myKeyInt:								;inturrupt to read keyboard
	push    eax        
	in      al,0x60						;retrieve a byte from port 0x60 (keyboard input)
	mov     byte [_keyVal],al
	mov     al,0x20						;send byte 0x20 to port 0x20 to reset the PIC
	out     0x20,al
	pop     eax
	ret
_myKeyInt_Size	dd	$-_myKeyInt			;calculates size of _myKeyInt($=this line)

drawScore:						;draws players score
	enter	0,0
	pushad
	mov		edx,0
	mov		eax,dword[score]
	mov		ebx,10
	idiv	ebx						;eax=10's digit,edx=1's digit
	add		eax,48					;get the ascii value, 0=48 in ascii
	add		edx,48					;1=49...etc
	push	edx
	push	eax
	call	drawAscii        
	popad
	leave
	ret
	
drawAscii:
	enter	0,0
	pushad
	mov		ecx,0		;first character
ol1:
	movzx   eax, byte [ebp + 8] ; load char into eax
    shl     eax, 3              ; eax *= 8
    add     eax, ecx            ; eax += i
	movzx   ebx, byte [gs:eax]
    mov     edx, 0              ; j
il1:
	mov     bh, bl
    and     bh, 80h
    cmp     bh, 0
	je      ile1
	mov     eax, ecx	    	 ; ecx ==> i
	add		eax,2		     	 ;start on row 2
    shl     eax, 10		     	 ; note: 1024 = 2^10
	add		eax,1004	     	 ;move right 1004 pixels
    add     eax, edx	     	 ; edx ==> j
	mov     [fs:eax], byte 16    ;initial draw was at 0,0 but now at 1004,2
ile1:
	shl     ebx, 1
    inc     edx			; increment j
    cmp     edx, 8		; end of byte
    jl      il1
    inc     ecx			; increment i
    cmp     ecx, 8		; end of 8 bitmap bytes
    jl      ol1
	mov		ecx,0		;second character
ol2:
	movzx   eax, byte [ebp + 12 ] ; load char into eax
    shl     eax, 3              ; eax *= 8
    add     eax, ecx            ; eax += i
	movzx   ebx, byte [gs:eax]
    mov     edx, 0              ; j
il2:
	mov     bh, bl
    and     bh, 80h
    cmp     bh, 0
	je      ile2
	mov     eax, ecx	   		 ; ecx ==> i
	add		eax,2		    	 ;move 2 rows down
    shl     eax, 10         	 ; note: 1024 = 2^10
	add		eax,1013	   		 ;move 1013 right
    add     eax, edx	     	 ; edx ==> j
	mov     [fs:eax], byte 16
ile2:
	shl     ebx, 1
    inc     edx					; increment j
    cmp     edx, 8				; end of byte
    jl      il2
    inc     ecx					; increment i
    cmp     ecx, 8				; end of 8 bitmap bytes
    jl      ol2	
	popad
	leave
	ret 8
	
mystart:
	setupXGA
	call	drawBackground
	push	dword [pacY]				;y
	push	dword [pacX]				;x
	call	drawPacman
	push	dword [trollY]				;y
	push	dword [trollX]				;x
	call	drawTrolls
starting:
	call 	clearFood					;draw new food after a trolls cycle
	call 	randomFood	
	mov		ecx,250						;start moving the trolls
moveTrollRight:
	call	checkinput	      			;check for user input
	cmp		byte[quit],1
	je		near end
	inc		dword[trollX]				;x++
	push	dword[trollY]				;y
	push	dword[trollX]				;x
	call 	waitVRetrace
	call	drawTrolls					;draw the moved trolls
	loop	moveTrollRight
	mov		ecx,275
moveTrollDown:
	call	checkinput	      			;check for user input
	cmp		byte[quit],1
	je		near end
	inc		dword[trollY]				;y++
	push	dword[trollY]				;y
	push	dword[trollX]				;x
	call 	waitVRetrace
	call	drawTrolls					;draw the moved trolls
	loop	moveTrollDown
	mov		ecx,250
moveTrollLeft:
	call	checkinput	      			;check for user input
	cmp		byte[quit],1
	je		near end
	dec		dword[trollX]				;x--
	push	dword[trollY]				;y
	push	dword[trollX]				;x
	call 	waitVRetrace
	call	drawTrolls					;draw the moved trolls
	loop	moveTrollLeft
	mov		ecx,275
moveTrollUp:
	call	checkinput	      			;check for user input
	cmp		byte[quit],1
	je		end
	dec		dword[trollY]				;y--
	push	dword[trollY]				;y
	push	dword[trollX]				;x
	call 	waitVRetrace
	call	drawTrolls					;draw the moved trolls
	loop	moveTrollUp
	jmp	starting
end:
	cleanXGA
	ret
	
checkinput:
	enter	0,0
	call	drawScore
	cmp		dword[hp],0					;health=0 so quit
	je		doquit
	cmp		dword[score],5	    		;score=20 so quit
	je		doquit
	cmp     byte [_keyVal],0x01			;press esc to quit
	je      doquit
	cmp     byte [_keyVal],0x48			;move up
	je      moveUp
	cmp     byte [_keyVal],0x4b			;move left
	je      near moveLeft
	cmp     byte [_keyVal],0x50			;move down
	je      near moveDown
	cmp     byte [_keyVal],0x4d			;move right
	je      near moveRight
checkinputend:
	call	checkFoodEat				;check to see if food was eaten after move
	call	checktroll1hit
	call	checktroll2hit
	call	checktroll3hit
	call	checktroll4hit
	call	checktroll5hit
	call	checktroll6hit
	call	pacmancolour
	mov	byte[_keyVal],0
ciee:
	leave
	ret
doquit:
	mov	byte[quit],1
	jmp ciee
	
moveUp:
	cmp	dword[pacY],1
	jle	noup
	jmp	near checkBoxUp
moveU:	
	dec		dword[pacY]					;y--	Up and left works and does not leave a trail
	dec		dword[pacY]					;y--	added extra y
	push	dword[pacY]					;y
	push	dword[pacX]					;x
	call	drawPacman					;draw
noup:
	jmp	checkinputend
	
moveLeft:
	cmp	dword[pacX],1
	jle	noleft
	jmp	near checkBoxLeft
moveL:	
	dec		dword[pacX]					;x--
	dec		dword[pacX]					;x--	added extra x
	push	dword[pacY]					;y
	push	dword[pacX]					;x
	call	drawPacmanLeft					;draw
noleft:
	jmp	checkinputend
	
moveDown:
	cmp	dword[pacY],732
	jge	nodown
	jmp	near checkBoxDown
moveD:	
	inc		dword[pacY]					;y++	down and right leave a trail
	inc		dword[pacY]					;y++	added extra y
	push	dword[pacY]					;y
	push	dword[pacX]					;x
	call	drawPacman					;draw
nodown:
	jmp	checkinputend
	
moveRight:
	cmp	dword[pacX],988
	jge	noright
	jmp	near checkBoxRight
moveR:	
	inc		dword[pacX]					;x++
	inc		dword[pacX]					;x++	added extra x
	push	dword[pacY]					;y
	push	dword[pacX]					;x
	call	drawPacman					;draw
noright:
	jmp	checkinputend

checkBoxUp:							;checks if there is no box to move into when pressing up
	cmp	dword[pacX],73				;-
	jl	near moveU
	cmp	dword[pacX],306				;+
	jl	near cmub
	cmp	dword[pacX],379
	jl	near moveU
	cmp	dword[pacX],611
	jle	near cmub
	cmp	dword[pacX],685
	jl	near moveU
	cmp	dword[pacX],917
	jle	near cmub
	cmp	dword[pacX],987
	jl	near moveU
cmub:
	cmp	dword[pacY],73				;-
	jl	near moveU
	cmp	dword[pacY],333				;+
	jle	near noup
	cmp	dword[pacY],403
	jl	near moveU
	cmp	dword[pacY],664
	jle	near noup
	cmp	dword[pacY],731
	jl	near moveU
	jmp	near noup
	
checkBoxLeft:							;checks if there is no box to move into when pressing left
	cmp	dword[pacY],71					;-
	jle	near moveL
	cmp	dword[pacY],331					;+
	jle	near cmlb
	cmp	dword[pacY],402
	jle	near moveL
	cmp	dword[pacY],662
	jle	near cmlb
	cmp	dword[pacY],733
	jle	near moveL
cmlb:
	cmp	dword[pacX],73
	jle	near moveL
	cmp	dword[pacX],306
	jle	near noleft
	cmp	dword[pacX],378
	jle	near moveL
	cmp	dword[pacX],612
	jle	near noleft
	cmp	dword[pacX],684
	jle	near moveL
	cmp	dword[pacX],918
	jle	near noleft
	cmp	dword[pacX],989
	jle	near moveL
	jmp	near noleft
	
checkBoxDown:							;checks if there is no box to move into when pressing down
	cmp	dword[pacX],73
	jl	near moveD
	cmp	dword[pacX],306
	jl	near cmdb
	cmp	dword[pacX],379
	jl	near moveD
	cmp	dword[pacX],611
	jle	near cmdb
	cmp	dword[pacX],685
	jl	near moveD
	cmp	dword[pacX],917
	jle	near cmdb
	cmp	dword[pacX],987
	jl	near moveD
cmdb:
	cmp	dword[pacY],69
	jl	near moveD
	cmp	dword[pacY],330
	jle	near nodown
	cmp	dword[pacY],402
	jl	near moveD
	cmp	dword[pacY],661
	jle	near nodown
	cmp	dword[pacY],731
	jl	near moveD
	jmp	near nodown
	
checkBoxRight:							;checks if there is no box to move into when pressing right
	cmp	dword[pacY],71
	jle	near moveR
	cmp	dword[pacY],331
	jle	near cmrb
	cmp	dword[pacY],402
	jle	near moveR
	cmp	dword[pacY],662
	jle	near cmrb
	cmp	dword[pacY],733
	jle	near moveR
cmrb:	
	cmp	dword[pacX],71
	jle	near moveR
	cmp	dword[pacX],306
	jl	near noright
	cmp	dword[pacX],376
	jle	near moveR
	cmp	dword[pacX],611
	jle	near noright
	cmp	dword[pacX],683
	jle	near moveR
	cmp	dword[pacX],917
	jle	near noright
	cmp	dword[pacX],989
	jle	near moveR
	jmp	near noright
	
pacmancolour:							;checking if pacman colour needs to update
	enter 0,0
	cmp	dword[updateC],0				;dont change hp, 0=dont change
	je	near paccolourend
	cmp	dword[hp],3					;go yellow
	je	paccyellow
	cmp	dword[hp],2					;go orange
	je	paccorange
	cmp	dword[hp],1					;go red
	je	near paccred
paccyellow:
	push 	eax
	mov	eax,dword[yellow] 				;change hp to yellow
	mov	dword[colour],eax
	push	dword 15 					;colour white
	push	dword 34					;width
	push	dword 34					;length
	push	dword[pacY]					;y
	push	dword[pacX]					;x
	call	drawRect					;drawing a white square where pacman was
	mov	dword[pacY],10				;moving pacman to start to show colour change
	mov	dword[pacX],10
	mov	dword[updateC],0
	jmp	paccolourend
paccorange:
	push	eax
	mov 	eax,dword[orange] 				;change hp to orange
	mov	dword[colour],eax
	pop	eax
	push	dword 15 					;colour white
	push	dword 34					;width
	push	dword 34					;length
	push	dword[pacY]					;y
	push	dword[pacX]					;x
	call	drawRect
	mov	dword[pacY],10
	mov	dword[pacX],10
	mov	dword[updateC],0
	jmp	paccolourend
paccred:
	push	eax
	mov 	eax,dword[red] 					;change hp to red
	mov	dword[colour],eax
	pop	eax
	push	dword 15 					;colour white
	push	dword 34					;width
	push	dword 34					;length
	push	dword[pacY]					;y
	push	dword[pacX]					;x
	call	drawRect
	mov	dword[pacY],10
	mov	dword[pacX],10
	mov	dword[updateC],0
paccolourend:
	mov	byte[updateC],0
	leave
	ret

checkFoodEat:							;checks if food was eaten
	enter	0,0
	cmp	dword[pacY],30					;food ranges from 10<=y<=30
	jle	checkX						
	jmp	checkFoodEnd					;not in range
checkX:
	cmp 	byte[foodpos],0				;check where the food is located
	je	near checkfood1
	cmp	byte[foodpos],1
	je	near checkfood2
	cmp	byte[foodpos],2
	je	near checkfood3
	cmp	byte[foodpos],3
	je	near checkfood4
	cmp	byte[foodpos],4
	je 	near checkfood5
	cmp 	byte[foodpos],5
	je	near checkfood6
	jmp	checkfood7
checkfood1:	
	cmp	dword[pacX],68				;check x axis if y was in range
	jle	near checkFoodEnd			;food(a)->a00<=x<=a31, 10<=y<=30
	cmp	dword[pacX],131				;pacman is 32x32
	jge	near checkFoodEnd			;checking if out of bounds here
	jmp	checkfoodend1
checkfood2:	
	cmp	dword[pacX],168				;y was checking if in bounds
	jle	near checkFoodEnd
	cmp	dword[pacX],231
	jge	near checkFoodEnd
	jmp	checkfoodend1
checkfood3:	
	cmp	dword[pacX],268
	jle	near checkFoodEnd
	cmp	dword[pacX],331
	jge	near checkFoodEnd
	jmp	checkfoodend1
checkfood4:	
	cmp	dword[pacX],368
	jle	near checkFoodEnd
	cmp	dword[pacX],431
	jge	near checkFoodEnd
	jmp	checkfoodend1
checkfood5:	
	cmp	dword[pacX],468
	jle	checkFoodEnd
	cmp	dword[pacX],531
	jge	checkFoodEnd
	jmp	checkfoodend1
checkfood6:	
	cmp	dword[pacX],568
	jle	checkFoodEnd
	cmp	dword[pacX],631
	jge	checkFoodEnd
	jmp	checkfoodend1
checkfood7:	
	cmp	dword[pacX],668
	jle	checkFoodEnd
	cmp	dword[pacX],731
	jge	checkFoodEnd
checkfoodend1:	
	call	clearFood		;pacman is intersecting so remove food
	add	dword[score],1		;increase score for eating food
	call	randomFood	
checkFoodEnd:	
	leave
	ret	

checktroll1hit:
	enter	0,0
	pushad
	mov	eax,dword[trollY]
	cmp	dword[pacY],eax			;check if top of pacman inside troll
	jl	checktroll1Y2
	add	eax,32
	cmp	dword[pacY],eax
	jle	checktroll1X
	jmp	checktrollhitend1
checktroll1Y2:
	mov	eax,dword[trollY]
	mov	ebx,dword[pacY]
	add	ebx,32
	cmp	ebx,eax					;check if bottom of pacman inside troll
	jl	near checktrollhitend1
	add	eax,32
	cmp	dword[pacY],eax
	jle	checktroll1X
	jmp	checktrollhitend1
checktroll1X:	
	mov	eax,dword[trollX]
	cmp	dword[pacX],eax			;check if left of pacman inside troll
	jl	checktroll1X2
	add	eax,32
	cmp	dword[pacX],eax
	jle	takehit1
	jmp	checktrollhitend1
checktroll1X2:
	mov	eax,dword[trollX]
	mov	ebx,dword[pacX]
	add	ebx,32
	cmp	ebx,eax					;check if right of pacman inside troll
	jl	checktrollhitend1
	add	eax,32
	cmp	dword[pacX],eax
	jle	takehit1
	jmp	checktrollhitend1
takehit1:
	mov	dword[updateC],1	
	sub	dword[hp],1
checktrollhitend1:	
	popad
	leave
	ret

checktroll2hit:
	enter	0,0
	pushad
	mov	eax,dword[trollY]
	cmp	dword[pacY],eax			;check if top of pacman inside troll
	jl	checktroll2Y2
	add	eax,32
	cmp	dword[pacY],eax
	jle	checktroll2X
	jmp	checktrollhitend2
checktroll2Y2:
	mov	eax,dword[trollY]
	mov	ebx,dword[pacY]
	add	ebx,32
	cmp	ebx,eax					;check if bottom of pacman inside troll
	jl	near checktrollhitend2
	add	eax,32
	cmp	dword[pacY],eax
	jle	checktroll2X
	jmp	checktrollhitend2
checktroll2X:	
	mov	eax,dword[trollX]
	add	eax,306
	cmp	dword[pacX],eax			;check if left of pacman inside troll
	jl	checktroll2X2
	add	eax,32
	cmp	dword[pacX],eax
	jle	takehit2
	jmp	checktrollhitend2
checktroll2X2:
	mov	eax,dword[trollX]
	add	eax,306
	mov	ebx,dword[pacX]
	add	ebx,32
	cmp	ebx,eax					;check if right of pacman inside troll
	jl	checktrollhitend2
	add	eax,32
	cmp	dword[pacX],eax
	jle	takehit2
	jmp	checktrollhitend2
takehit2:
	mov	dword[updateC],1	
	sub	dword[hp],1
checktrollhitend2:	
	popad
	leave
	ret
	
checktroll3hit:
	enter	0,0
	pushad
	mov	eax,dword[trollY]
	cmp	dword[pacY],eax			;check if top of pacman inside troll
	jl	checktroll3Y2
	add	eax,32
	cmp	dword[pacY],eax
	jle	checktroll3X
	jmp	checktrollhitend3
checktroll3Y2:
	mov	eax,dword[trollY]
	mov	ebx,dword[pacY]
	add	ebx,32
	cmp	ebx,eax					;check if bottom of pacman inside troll
	jl	near checktrollhitend3
	add	eax,32
	cmp	dword[pacY],eax
	jle	checktroll3X
	jmp	checktrollhitend3
checktroll3X:	
	mov	eax,dword[trollX]
	add	eax,306+306
	cmp	dword[pacX],eax			;check if left of pacman inside troll
	jl	checktroll3X2
	add	eax,32
	cmp	dword[pacX],eax
	jle	takehit3
	jmp	checktrollhitend3
checktroll3X2:
	mov	eax,dword[trollX]
	add	eax,306+306
	mov	ebx,dword[pacX]
	add	ebx,32
	cmp	ebx,eax					;check if right of pacman inside troll
	jl	checktrollhitend3
	add	eax,32
	cmp	dword[pacX],eax
	jle	takehit3
	jmp	checktrollhitend3
takehit3:
	mov	dword[updateC],1	
	sub	dword[hp],1
checktrollhitend3:	
	popad
	leave
	ret
	
checktroll4hit:
	enter	0,0
	pushad
	mov	eax,dword[trollY]
	add	eax,331
	cmp	dword[pacY],eax			;check if top of pacman inside troll
	jl	checktroll4Y2
	add	eax,32
	cmp	dword[pacY],eax
	jle	checktroll4X
	jmp	checktrollhitend4
checktroll4Y2:
	mov	eax,dword[trollY]
	add	eax,331
	mov	ebx,dword[pacY]
	add	ebx,32
	cmp	ebx,eax					;check if bottom of pacman inside troll
	jl	near checktrollhitend4
	add	eax,32
	cmp	dword[pacY],eax
	jle	checktroll4X
	jmp	checktrollhitend4
checktroll4X:	
	mov	eax,dword[trollX]
	cmp	dword[pacX],eax			;check if left of pacman inside troll
	jl	checktroll4X2
	add	eax,32
	cmp	dword[pacX],eax
	jle	takehit4
	jmp	checktrollhitend4
checktroll4X2:
	mov	eax,dword[trollX]
	mov	ebx,dword[pacX]
	add	ebx,32
	cmp	ebx,eax					;check if right of pacman inside troll
	jl	checktrollhitend4
	add	eax,32
	cmp	dword[pacX],eax
	jle	takehit4
	jmp	checktrollhitend4
takehit4:
	mov	dword[updateC],1	
	sub	dword[hp],1
checktrollhitend4:	
	popad
	leave
	ret
	
checktroll5hit:
	enter	0,0
	pushad
	mov	eax,dword[trollY]
	add	eax,331
	cmp	dword[pacY],eax			;check if top of pacman inside troll
	jl	checktroll5Y2
	add	eax,32
	cmp	dword[pacY],eax
	jle	checktroll5X
	jmp	checktrollhitend5
checktroll5Y2:
	mov	eax,dword[trollY]
	add	eax,331
	mov	ebx,dword[pacY]
	add	ebx,32
	cmp	ebx,eax					;check if bottom of pacman inside troll
	jl	near checktrollhitend5
	add	eax,32
	cmp	dword[pacY],eax
	jle	checktroll5X
	jmp	checktrollhitend5
checktroll5X:	
	mov	eax,dword[trollX]
	add	eax,306
	cmp	dword[pacX],eax			;check if left of pacman inside troll
	jl	checktroll5X2
	add	eax,32
	cmp	dword[pacX],eax
	jle	takehit5
	jmp	checktrollhitend5
checktroll5X2:
	mov	eax,dword[trollX]
	add	eax,306
	mov	ebx,dword[pacX]
	add	ebx,32
	cmp	ebx,eax					;check if right of pacman inside troll
	jl	checktrollhitend5
	add	eax,32
	cmp	dword[pacX],eax
	jle	takehit5
	jmp	checktrollhitend5
takehit5:
	mov	dword[updateC],1	
	sub	dword[hp],1
checktrollhitend5:	
	popad
	leave
	ret
	
checktroll6hit:
	enter	0,0
	pushad
	mov	eax,dword[trollY]
	add	eax,331
	cmp	dword[pacY],eax			;check if top of pacman inside troll
	jl	checktroll6Y2
	add	eax,32
	cmp	dword[pacY],eax
	jle	checktroll6X
	jmp	checktrollhitend6
checktroll6Y2:
	mov	eax,dword[trollY]
	add	eax,331
	mov	ebx,dword[pacY]
	add	ebx,32
	cmp	ebx,eax					;check if bottom of pacman inside troll
	jl	near checktrollhitend6
	add	eax,32
	cmp	dword[pacY],eax
	jle	checktroll6X
	jmp	checktrollhitend6
checktroll6X:	
	mov	eax,dword[trollX]
	add	eax,306+306
	cmp	dword[pacX],eax			;check if left of pacman inside troll
	jl	checktroll6X2
	add	eax,32
	cmp	dword[pacX],eax
	jle	takehit6
	jmp	checktrollhitend6
checktroll6X2:
	mov	eax,dword[trollX]
	add	eax,306+306
	mov	ebx,dword[pacX]
	add	ebx,32
	cmp	ebx,eax					;check if right of pacman inside troll
	jl	checktrollhitend6
	add	eax,32
	cmp	dword[pacX],eax
	jle	takehit6
	jmp	checktrollhitend6
takehit6:
	mov	dword[updateC],1	
	sub	dword[hp],1
checktrollhitend6:	
	popad
	leave
	ret
	
clearFood:								;clear the food positions
	enter 0,0
	cmp 	byte[foodpos],0				;check where the food is located
	je		near clsZero
	cmp		byte[foodpos],1
	je		near clsOne
	cmp		byte[foodpos],2
	je		near clsTwo
	cmp		byte[foodpos],3
	je		near clsThree
	cmp		byte[foodpos],4
	je 		near clsFour
	cmp 	byte[foodpos],5
	je		near clsFive
	jmp		clsSix	
clsZero:
	push	dword 15					;colour white
	push	dword 40					;width
	push	dword 40					;length
	push	dword 10					;y
	push	dword 100					;x
	call	drawRect
	jmp		clsEnd
clsOne:	
	push	dword 15					;colour white
	push	dword 40					;width
	push	dword 40					;length
	push	dword 10					;y
	push	dword 200					;x
	call	drawRect
	jmp		clsEnd	
clsTwo:
	push	dword 15					;colour white
	push	dword 40					;width
	push	dword 40					;length
	push	dword 10					;y
	push	dword 300					;x
	call	drawRect
	jmp		clsEnd
clsThree:	
	push	dword 15					;colour white
	push	dword 40					;width
	push	dword 40					;length
	push	dword 10					;y
	push	dword 400					;x
	call	drawRect
	jmp		clsEnd
clsFour:	
	push	dword 15					;colour white
	push	dword 40					;width
	push	dword 40					;length
	push	dword 10					;y
	push	dword 500					;x
	call	drawRect	
	jmp		clsEnd
clsFive:
	push	dword 15					;colour white
	push	dword 40					;width
	push	dword 40					;length
	push	dword 10					;y
	push	dword 600					;x
	call	drawRect
	jmp		clsEnd
clsSix:
	push	dword 15					;colour white
	push	dword 40					;width
	push	dword 40					;length
	push	dword 10					;y
	push	dword 700					;x
	call	drawRect
clsEnd:	
	push	dword 15					;colour white
	push	dword 20					;width
	push	dword 20					;length
	push	dword 0						;y
	push	dword 1000					;x
	call	drawRect					;clearing previous score
	leave
	ret
	
waitVRetrace:							;wait for the writing phase
    push    ebp
    mov     ebp, esp
    pushad
    mov     dx, 0x3DA					;input port in dx register
.loop1:        
    in      eax, dx						;read from port 0x3DA
    and     eax, 0x08					;keep looping while 4th bit is set
    cmp     eax, 0
    jne     .loop1
.loop2:
    in      eax, dx						;read byte from port
    and     eax, 0x08					;keep looping while 4th bit is clear
    cmp     eax, 0
    je      .loop2
    popad
    mov     esp, ebp
    pop		ebp
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
	push	dword 15 					;colour white
	push	dword 34					;width
	push	dword 34					;length
	push	dword[ebp+12]				;y
	push	dword[ebp+8]				;x
	call	drawRect
	push	dword [colour] 					;colour YELLOW
	push	dword 30					;width
	push	dword 30					;length
	add		dword[ebp+12],2				;y offset
	add		dword[ebp+8],2				;x offset
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

drawPacmanLeft:
	enter	0,0							;args((+8)x,(+12)y)
	push	dword 15 					;colour white
	push	dword 34					;width
	push	dword 34					;length
	push	dword[ebp+12]				;y
	push	dword[ebp+8]				;x
	call	drawRect
	push	dword [colour] 					;colour YELLOW
	push	dword 30					;width
	push	dword 30					;length
	add		dword[ebp+12],2				;y offset
	add		dword[ebp+8],2				;x offset
	push	dword[ebp+12]				;y
	push	dword[ebp+8]				;x
	call	drawRect					;draw body
	push	dword 0						;colour BLACK
	push	dword 5						;width
	push	dword 5						;length
	add		dword[ebp+12],7				;y offset eye
	add		dword[ebp+8],18				;x offset eye
	push	dword[ebp+12]				;y
	push	dword[ebp+8]				;x
	call	drawRect					;draw eye feature
	push	dword 0						;colour BLACK
	push	dword 15					;width
	push	dword 7						;length
	add		dword[ebp+12],10			;y offset mouth
	sub		dword[ebp+8],18				;x offset mouth
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
	add		dword[ebp+12],1				;y offset 
	add		dword[ebp+8],1				;x offset 
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
	mov		byte[foodpos],al
	cmp 	eax, 0
	je		near posZero
	cmp		eax, 1
	je		near posOne
	cmp		eax, 2
	je		near posTwo
	cmp		eax, 3
	je		near posThree
	cmp		eax, 4
	je		near posFour
	cmp 	eax, 5
	je		near posFive
	jmp		near posSix
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
	popad
	leave
	ret

[SECTION .data]
_keyVal	dd		0						;value read from keyboard
pacX	dd		10						;current position of pacman x coordinate
pacY	dd		10						;current position of pacman y coordinate
trollX	dd		66						;current position of troll x coordinate
trollY	dd		66						;current position of troll y coordinate
hp		dd		4						;starting health
score	dd		0						;starting score
colour	dd		47						;current pacman colour
updateC	dd		0		 				;boolean to change pacman hp
green	dd		47						;green
yellow 	dd		44						;yellow
orange	dd		42						;orange
red		dd		40						;red
quit	db		0						;quit flag
[SECTION .bss]
vidmode	resb	1						;old video mode
foodpos	resb	1						;place where food is at