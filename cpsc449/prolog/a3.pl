:- dynamic(header/1,
	   fpam/1,
	   fpa/1,
	   ftp/1,
	   tnt/1,
	   fm/1,
	   tgood/1,
	   fms/1,
	   cFile/1,
	   titFound/1,
	   fpat/1,
	    tnps/2,
	    tnp/1, 
	    outfile/1,
		infile/1,
	    mp0/1,
	    mp1/1,
	    mp2/1,
	    mp3/1,
	    mp4/1,
	    mp5/1,
	    mp6/1,
	    mp7/1,
	    bestPen/1,
	    bestSol/1,
	    tempval/1,
	     sum/1,
		utask/1,
		fpaSol/1,
		permSize/1,
		taskList/1,
		 workList/1,fmcount/2,
		 index/1 ).

    :- initialization(main).
    main :- 
    argument_value(1, INFILE),!,
    argument_value(2, OUTFILE),!,
	asserta(infile(INFILE)),!,
    asserta(outfile(OUTFILE)),!,
	
    getinput(INFILE),!,
    checkHeaders, 
    enumerate,
    %bestSol(X),
    bestPen(Y),
    (Y = 999999
    -> nvp
    ; pvs
    ),
    halt(0).

    getinput(File) :-
    open(File,read,Str),
    readFile(Str, []),
    close(Str).

epf :-
	outfile(X),
	open(X,write,Stream),
	write(Stream,'Error while parsing input file'),
	close(Stream),
	halt(-1).

pvs :-
	outfile(X),
	open(X,write,Stream),
	write(Stream,'Solution '),
	bestSol(S),
	item_at(1,S,A),write(Stream,A), write(Stream,' '),
	item_at(2,S,B),write(Stream,B), write(Stream,' '),
	item_at(3,S,C),write(Stream,C), write(Stream,' '),
	item_at(4,S,D),write(Stream,D), write(Stream,' '),
	item_at(5,S,E),write(Stream,E), write(Stream,' '),
	item_at(6,S,F),write(Stream,F), write(Stream,' '),
	item_at(7,S,G),write(Stream,G), write(Stream,' '),
	item_at(8,S,H),write(Stream,H), write(Stream,'; Quality: '),
	bestPen(Y),write(Stream,Y),
	close(Stream),
	halt(-1).

imt :-
	outfile(X),
	open(X,write,Stream),
	write(Stream,'invalid machine/task'),
	close(Stream),
	halt(-1).

mpe :-
	outfile(X),
	open(X,write,Stream),
	write(Stream,'machine penalty error'),
	close(Stream),
	halt(-1).

ip :-
	outfile(X),
	open(X,write,Stream),
	write(Stream,'invalid penalty'),
	close(Stream),
	halt(-1).

pae :-
	outfile(X),
	open(X,write,Stream),
	write(Stream,'partial assignment error'),
	close(Stream),
	halt(-1).
it :-
	outfile(X),
	open(X,write,Stream),
	write(Stream,'invalid task'),
	close(Stream),
	halt(-1).
nvp :-
	outfile(X),
	open(X,write,Stream),
	write(Stream,'No valid solution possible!'),
	close(Stream),
	halt(-1).


    readFile(Stream, X) :-
    readWord(Stream, Line),
    (Line = ''
     -> readFile(Stream,X)
     ;
	 (Line = 'Name:'
       -> assertz(header(name)), readFile(Stream,X)
       ;(Line = 'forcedpartialassignment:'
	 -> assertz(header(fpa)),dofpas(Stream, X),!
	 ;(Line = 'a'
	   -> !
	   ;(titFound(y)
	     -> epf%write('Error while parsing input file'), halt(-1)
	     ;assertz(titFound(y)), readFile(Stream,X)
	   )
	 )
       )
     )
    ).

   dofpas(Stream, Y) :-
    readWord(Stream, Line),
    %write(Line),nl,
    (Line = ''
     ->dofpas(Stream, Y)
     ;
     (Line = 'forbiddenmachine:'
      -> assertz(header(fm)),dofms(Stream, Y)
      ;  
      (Line = 'a'
       -> !
       ;
       atom_chars(Line, QQ),
       item_at(1, QQ, QQMORE),
       (QQMORE \= '('
	-> epf%write('Error while parsing input file'), halt(-1)
	;
	(validpair(Line)
	 ->atom_chars(Line, F),
	 item_at(2,F,G),
	 item_at(4,F,Q),
	 (fpam(G)
	  -> pae%write('partial assignment error'),halt(-1)
	  ; 
	  (fpat(Q)
	  -> pae%write('partial assignment error'),halt(-1)
	  ; 
	  retract(utask(Q)), fpaSol(FP),  valcon(G,V), item_at(1,FP, Po1),item_at(2,FP, Po2),item_at(3,FP, Po3),
	  item_at(4,FP, Po4),item_at(5,FP, Po5),item_at(6,FP, Po6),item_at(7,FP, Po7),item_at(8,FP, Po8),
	  permSize(PZ), PK is PZ - 1, retract(permSize(PZ)), asserta(permSize(PK)),
	  (V = 1
	  -> retract(fpaSol([Po1,Po2,Po3,Po4,Po5,Po6,Po7,Po8])), asserta(fpaSol([Q,Po2,Po3,Po4,Po5,Po6,Po7,Po8]))
	  ;(V = 2
	  -> retract(fpaSol([Po1,Po2,Po3,Po4,Po5,Po6,Po7,Po8])), asserta(fpaSol([Po1,Q,Po3,Po4,Po5,Po6,Po7,Po8])) 
	  ;(V = 3
	  ->  retract(fpaSol([Po1,Po2,Po3,Po4,Po5,Po6,Po7,Po8])), asserta(fpaSol([Po1,Po2,Q,Po4,Po5,Po6,Po7,Po8]))
	  ;(V = 4
	  ->  retract(fpaSol([Po1,Po2,Po3,Po4,Po5,Po6,Po7,Po8])), asserta(fpaSol([Po1,Po2,Po3,Q,Po5,Po6,Po7,Po8]))
	  ;(V = 5
	  ->  retract(fpaSol([Po1,Po2,Po3,Po4,Po5,Po6,Po7,Po8])), asserta(fpaSol([Po1,Po2,Po3,Po4,Q,Po6,Po7,Po8]))
	  ;(V = 6
	  ->  retract(fpaSol([Po1,Po2,Po3,Po4,Po5,Po6,Po7,Po8])), asserta(fpaSol([Po1,Po2,Po3,Po4,Po5,Q,Po7,Po8]))
	  ;(V = 7
	  ->  retract(fpaSol([Po1,Po2,Po3,Po4,Po5,Po6,Po7,Po8])), asserta(fpaSol([Po1,Po2,Po3,Po4,Po5,Po6,Q,Po8]))
	  ;(V =  8
	  ->  retract(fpaSol([Po1,Po2,Po3,Po4,Po5,Po6,Po7,Po8])), asserta(fpaSol([Po1,Po2,Po3,Po4,Po5,Po6,Po7,Q]))
	  ; !
	  )
	  )
	  )
	  )
	  )
	  )
	  )
	  ),
	  assertz(fpam(G)),assertz(fpat(Q)),assertz(fpa(['(',G,',',Q,')'])),dofpas(Stream,Y)
	 )
	 ;imt%write('invalid machine/task'),halt(-1)
	)
	)
       )
      )
     )
    ).
    
    
   dofms(Stream, Y) :-
    readWord(Stream, Line),
    %write(Line),nl,
    (Line = ''
     ->dofms(Stream, Y)
     ;
     (Line = 'too-neartasks:'
      -> assertz(header(tnt)),dotnts(Stream, Y)
      ;  
      (Line = 'a'
       -> !
       ;
       (validpair(Line)
	-> atom_chars(Line, F), 
	item_at(2,F,G),%INVALIDFORBIDDEN FIX 6 LINES
	 item_at(4,F,Q),
	 retract(fmcount(G,XX)),XXI is XX + 1,
		retract(fmcount(Q,XXX)), XXXI is XXX + 1,
		assertz(fmcount(G,XXI)),
		assertz(fmcount(Q,XXXI)),
		(fpa(['(',G,',',Q,')'])->nvp;!),
	 assertz(fm(F)),dofms(Stream,Y)
	;atom_chars(Line, F), length(F,FU),(FU = 5 
	->imt%write('invalid machine/task'),halt(-1)
	;epf
	)
       )
      )
     )
    ).
    
    dotnts(Stream, Y) :-
    readWord(Stream, Line),
    %write(Line),nl,
    (Line = ''
     ->dotnts(Stream, Y)
     ;
     (Line = 'machinepenalties:'
      -> Counter is 0, assertz(header(mp)),domps(Stream, Y, Counter)
      ;  
      (Line = 'a'
       -> epf
       ;
       (validtask(Line)
	->atom_chars(Line, F), assertz(tnt(F)),dotnts(Stream,Y)
	;atom_chars(Line, F), length(F,FU),(FU = 5 
	->imt%write('invalid machine/task'),halt(-1)
	;epf
	)
       )
      )
     )
    ).
    
    domps(Stream, Y, Counter) :-
    readWordMp(Stream, Line),
    (Counter = 9
     -> mpe%write('machine penalty error'),halt(-1)
     ;
     (Line = ''
      ->domps(Stream, Y, Counter)
      ;
      (Line = 'too-near penalities:'
       -> epf%write('Error while parsing intput file'), halt(-1)
       ;
       (Line = 'too-near penalities'
	-> assertz(header(tnp)),dotnps(Stream, Y)
	;  
	(Line = 'a'
	 -> !
	 ;
	 atom_codes(Line, LO),
	 split(LO," ", S),
	 length(S, Z),
	 %write(S),nl,
	 (Z = 8
	  -> item_at(1,S,L_ONE), item_at(2,S,L_TWO), item_at(3,S,L_THR), item_at(4,S,L_FOR), item_at(5,S,L_FIV), item_at(6,S,L_SIX), item_at(7,S,L_SEV), item_at(8,S,L_EHT),
	  (isalldigits(L_ONE),isalldigits(L_TWO), isalldigits(L_THR), isalldigits(L_FOR), isalldigits(L_FIV), isalldigits(L_SIX), isalldigits(L_SEV), isalldigits(L_EHT) 
	   -> 
	   number_codes(ONE, L_ONE),number_codes(TWO, L_TWO),number_codes(THR, L_THR),number_codes(FOR, L_FOR),number_codes(FIV, L_FIV),number_codes(SIX, L_SIX),number_codes(SEV, L_SEV),number_codes(EHT, L_EHT),
	   (Counter = 0
	    -> assertz(mp0([ONE,TWO,THR,FOR,FIV,SIX,SEV,EHT])),C is Counter+1,domps(Stream,Y, C)
	    ;(Counter = 1
	      -> assertz(mp1([ONE,TWO,THR,FOR,FIV,SIX,SEV,EHT])),C is Counter+1,domps(Stream,Y, C)
	      ;(Counter = 2
		-> assertz(mp2([ONE,TWO,THR,FOR,FIV,SIX,SEV,EHT])),C is Counter+1,domps(Stream,Y, C)
		;(Counter = 3
		  -> assertz(mp3([ONE,TWO,THR,FOR,FIV,SIX,SEV,EHT])),C is Counter+1,domps(Stream,Y, C)
		  ;
		  (Counter = 4
		   -> assertz(mp4([ONE,TWO,THR,FOR,FIV,SIX,SEV,EHT])),C is Counter+1,domps(Stream,Y, C)
		   ;
		   (Counter = 5
		    -> assertz(mp5([ONE,TWO,THR,FOR,FIV,SIX,SEV,EHT])),C is Counter+1,domps(Stream,Y, C)
		    ;
		    (Counter = 6
		     -> assertz(mp6([ONE,TWO,THR,FOR,FIV,SIX,SEV,EHT])),C is Counter+1,domps(Stream,Y, C)
		     ;(Counter = 7
		       -> assertz(mp7([ONE,TWO,THR,FOR,FIV,SIX,SEV,EHT])),C is Counter+1,domps(Stream,Y, C)
			   ;mpe
		     )
		    )
		   )
		  )
		)
	      )
	    )
	   )
	   ;length(L_EHT, DD),
	    (DD = 0
	    -> mpe%write('machine penalty error'),halt(-1)
	    ;ip%write('invalid penalty'),halt(-1)
	    )
	  )
	 )
	 ;mpe%write('machine penalty error'),halt(-1)
	)
       )
      )
     )
    ).
    
isalldigits([]) :- fail.
isalldigits(L) :-
	[H|T] = L,
	H >= 48,
	H =< 57,
	isalldigits1(T).
isalldigits1([]) :- !.
isalldigits1(L) :-
	[H|T] = L,
	H >= 48,
	H =< 57,
	isalldigits1(T).

    dotnps(Stream, Y) :-
    readWord(Stream, Line),
    %write(Line),nl,
    (Line = ''
     ->dotnps(Stream, Y)
     ;
     (Line = 'a'
      -> assertz(cFile(done)),!
      ;
      atom_chars(Line, F),
      item_at(2,F,G),
      item_at(4,F,H),
      item_at(6,F,PEN),
      (exists(G),exists(H)
       -> (penexists(PEN)
	   -> atom_chars(Line, F), assertz(tnp(F)),dotnps(Stream,Y)
	   ;ip%write('invalid penalty'),halt(-1)
       )
       ;it%write('invalid task'),halt(-1)
      )
     )
    ).
    
    /*code taken from http://swi-prolog.996271.n3.nabble.com/splitting-Lists-based-on-some-delimitor-td902.html*/

    split(String, "", Result) :- !,
    characters_to_strings(String, Result).
    split(String, Delimiters, Result) :-
    real_split(String, Delimiters, Result).

    characters_to_strings([], []).
    characters_to_strings([C|Cs], [[C]|Ss]) :-
    characters_to_strings(Cs, Ss).

    real_split(String, Delimiters, Result) :-
    (   append(Substring, [Delimiter|Rest], String),
	memberchk(Delimiter, Delimiters)
        ->  Result = [Substring|Substrings],
	real_split(Rest, Delimiters, Substrings)
        ;   Result = [String]
    ).

    %end borrowed code

enumerate:-
	fmcount('A',X1),%INVALIDFORBIDDEN FIX 2 LINES
	(X1==8->nvp;!),
	genList,
	taskList(TJ),
	%length(TJ, Ts),
	item_at(1,TJ,To),item_at(1,To,Tk),
	%write(TJ),nl,
	(Tk \= 'Z'
	-> retract(taskList(TJ)),genList, taskList(TL), length(TL, Ts),
	%write(TL),nl,
	(Ts = 0
	-> !
	;(Ts = 1
	-> item_at(1,TL,Z1), item_at(1,Z1,L1), TLF = [L1]
	;(Ts = 2
	->item_at(1,TL,Z12), item_at(2,TL,Z22),
		item_at(1,Z12, C12), item_at(1,Z22, C22), TLF = [C12, C22]
	;(Ts = 3
	->item_at(1,TL,Z13), item_at(2,TL,Z23), item_at(3,TL,Z33),
	item_at(1,Z13,C13), item_at(1,Z23,C23), item_at(1,Z33,C33), TLF = [C13, C23, C33]
	;(Ts = 4
	->item_at(1,TL,Z14), item_at(2,TL,Z24), item_at(3,TL,Z34), item_at(4,TL,Z44), 
	item_at(1,Z14,C14), item_at(1,Z24,C24), item_at(1,Z34,C34), item_at(1,Z44, C44), TLF = [C14, C24, C34, C44]
	;(Ts = 5
	->item_at(1,TL,Z15), item_at(2,TL,Z25), item_at(3,TL,Z35), item_at(4,TL,Z45), item_at(5,TL,Z55),
	item_at(1,Z15, C15), item_at(1,Z25, C25), item_at(1,Z35,C35), item_at(1,Z45,C45), item_at(1,Z55, C55),	TLF = [C15, C25, C35, C45, C55]
	;(Ts = 6
	->item_at(1,TL,Z16), item_at(2,TL,Z26), item_at(3,TL,Z36),item_at(4,TL,Z46), item_at(5,TL,Z56), item_at(6,TL,Z66), 
	item_at(1,Z16,C16), item_at(1,Z26,C26), item_at(1,Z36,C36),item_at(1,Z46,C46), item_at(1,Z56,C56), item_at(1,Z66,C66), TLF = [C16, C26, C36, C46, C56, C66]
	;(Ts = 7
	->item_at(1,TL,Z17), item_at(2,TL,Z27), item_at(3,TL,Z37), item_at(4,TL,Z47), item_at(5,TL,Z57), item_at(6,TL,Z67), item_at(7,TL,Z77), 
	item_at(1,Z17, C17), item_at(1,Z27, C27), item_at(1,Z37,C37), item_at(1,Z47,C47), item_at(1,Z57,C57), item_at(1,Z67, C67), item_at(1,Z77,C77), TLF = [C17, C27, C37, C47, C57, C67, C77]
	;(Ts = 8
	->item_at(1,TL,Z18), item_at(2,TL,Z28), item_at(3,TL,Z38), item_at(4,TL,Z48), item_at(5,TL,Z58), item_at(6,TL,Z68), item_at(7,TL,Z78), item_at(8,TL,Z88),
item_at(1,Z18, C18), item_at(1,Z28,C28), item_at(1,Z38,C38), item_at(1,Z48,C48), item_at(1,Z58,C58), item_at(1,Z68,C68), item_at(1,Z78,C78), item_at(1,Z88,C88),	TLF = [C18, C28, C38, C48, C58, C68, C78, C88]
	; TLF = ['E','E','E','E','E','E','E','E']
	)
	)
	)
	)
	)
	)
	)
	)
	),
	%write(TLF),nl,
    findall(PERM, permutation(TLF,PERM), L),length(L, DI), SIZ is DI+1 %write('A'),%nl
	%write(L),%nl,
	; fpaSol(Ly), L = [Ly|[]],SIZ = 2%, write('nosol'),nl
	),
	%write(SIZ),%nl,
    hardcon(L, 1, SIZ).

genList:-
	   (utask('A')
	   ->(taskList(XX)
		  -> taskList(XX), retract(taskList(XX)), FF = [['A'] | XX], asserta(taskList(FF))
		  ; asserta(taskList([['A']]))
		  )
	   ; !
	   ),
	   (utask('B')
	   -> (taskList(XXb)
		  -> taskList(XXb), retract(taskList(XXb)), FFb = [['B'] | XXb], asserta(taskList(FFb))
		  ; asserta(taskList([['B']]))
		  )
	   ; !
	   ),
	   (utask('C')
	   -> (taskList(XXc)
		  ->  taskList(XXc), retract(taskList(XXc)), FFc = [['C'] | XXc], asserta(taskList(FFc))
		  ; asserta(taskList([['C']]))
		  )
	   ; !
	   ),
	   (utask('D')
	   ->(taskList(XXd)
		  -> taskList(XXd), retract(taskList(XXd)), FFd = [['D'] | XXd], asserta(taskList(FFd))
		  ; asserta(taskList([['D']])) 
		  )
	   ; !
	   ),
	   (utask('E')
	   -> (taskList(XXe)
		  -> taskList(XXe), retract(taskList(XXe)), FFe = [['E'] | XXe], asserta(taskList(FFe))
		  ; asserta(taskList([['E']]))
		  )
	   ; !
	   ),
	   (utask('F')
	   -> (taskList(XXf)
		  -> taskList(XXf), retract(taskList(XXf)), FFf = [['F'] | XXf], asserta(taskList(FFf))
		  ; asserta(taskList([['F']]))
		  )
	   ; !
	   ),
	   (utask('G')
	   -> (taskList(XXg)
		  -> taskList(XXg), retract(taskList(XXg)), FFg = [['G'] | XXg], asserta(taskList(FFg))
		  ; asserta(taskList([['G']]))
		  )
	   ; !
	   ),
	   (utask('H')
	   -> (taskList(XXh)
		  -> taskList(XXh), retract(taskList(XXh)), FFh = [['H'] | XXh], asserta(taskList(FFh))
		  ; asserta(taskList([['H']]))
		  )
	   ; !
	   ).
	
    hardcon(Ls, Counter, Size):-
	%write(Ls),%nl,
	reverse(Ls, Snub),
    item_at(Counter,Snub,It),
	%write(It),nl,
	genWork(It),
	workList(I),
    %write(I),nl,
    C is Counter + 1,
    (tnts(I)
     -> (fms(I)
	 -> (fpas(I)
	    -> softcon(I)
	    ; !
	    )
	 ; !
     )
     ; !
    ),
    (C == Size
     -> !
     ;hardcon(Ls, C, Size)
    ).
  
  
genWork(Item):-
	index(Y),
	retract(index(Y)),
	%write('SOME'),
	asserta(index(1)),
	fpaSol(FL),
	workList(WK),
	item_at(1,WK,T1),
	item_at(2,WK,T2),
	item_at(3,WK,T3),
	item_at(4,WK,T4),
	item_at(5,WK,T5),
	item_at(6,WK,T6),
	item_at(7,WK,T7),
	item_at(8,WK,T8),
	retract(workList([T1,T2,T3,T4,T5,T6,T7,T8])),
	asserta(workList(FL)),
	item_at(1,FL,S1),
	item_at(2,FL,S2),
	item_at(3,FL,S3),
	item_at(4,FL,S4),
	item_at(5,FL,S5),
	item_at(6,FL,S6),
	item_at(7,FL,S7),
	item_at(8,FL,S8),
	(S1 = 'R'
	->	workList(WL), index(Id), item_at(Id,Item,Fo),retract(index(Id)), Newid is Id+1, asserta(index(Newid)),
		item_at(1,WL,Q1),item_at(2,WL,Q2),item_at(3,WL,Q3),item_at(4,WL,Q4),
		item_at(5,WL,Q5),item_at(6,WL,Q6),item_at(7,WL,Q7),item_at(8,WL,Q8),
		retract(workList([Q1,Q2,Q3,Q4,Q5,Q6,Q7,Q8])), asserta(workList([Fo,Q2,Q3,Q4,Q5,Q6,Q7,Q8])) 
	; !
	),
	(S2 = 'R'
	->	workList(WL1), index(Id2), item_at(Id2,Item,Fo2),retract(index(Id2)), Newid2 is Id2+1, asserta(index(Newid2)),
		item_at(1,WL1,Q12),item_at(2,WL1,Q22),item_at(3,WL1,Q32),item_at(4,WL1,Q42),
		item_at(5,WL1,Q52),item_at(6,WL1,Q62),item_at(7,WL1,Q72),item_at(8,WL1,Q82),
		retract(workList([Q12,Q22,Q32,Q42,Q52,Q62,Q72,Q82])), asserta(workList([Q12,Fo2,Q32,Q42,Q52,Q62,Q72,Q82])) 
	; !
	),
	(S3 = 'R'
	->	workList(WL2), item_at(Id3,Item,Fo3),retract(index(Id3)), Newid3 is Id3+1, asserta(index(Newid3)),
		item_at(1,WL2,Q13),item_at(2,WL2,Q23),item_at(3,WL2,Q33),item_at(4,WL2,Q43),
		item_at(5,WL2,Q53),item_at(6,WL2,Q63),item_at(7,WL2,Q73),item_at(8,WL2,Q83),
		retract(workList([Q13,Q23,Q33,Q43,Q53,Q63,Q73,Q83])), asserta(workList([Q13,Q23,Fo3,Q43,Q53,Q63,Q73,Q83]))
	; !
	),
	(S4 = 'R'
	-> workList(WL3), index(Id4), item_at(Id4,Item,Fo4),retract(index(Id4)), Newid4 is Id4+1, asserta(index(Newid4)),
		item_at(1,WL3,Q14),item_at(2,WL3,Q24),item_at(3,WL3,Q34),item_at(4,WL3,Q44),
		item_at(5,WL3,Q54),item_at(6,WL3,Q64),item_at(7,WL3,Q74),item_at(8,WL3,Q84),
		retract(workList([Q14,Q24,Q34,Q44,Q54,Q64,Q74,Q84])), asserta(workList([Q14,Q24,Q34,Fo4,Q54,Q64,Q74,Q84])) 
	; !
	),
	(S5 = 'R'
	->	workList(WL4), index(Id5), item_at(Id5,Item,Fo5),retract(index(Id5)), Newid5 is Id5+1, asserta(index(Newid5)),
		item_at(1,WL4,Q15),item_at(2,WL4,Q25),item_at(3,WL4,Q35),item_at(4,WL4,Q45),
		item_at(5,WL4,Q55),item_at(6,WL4,Q65),item_at(7,WL4,Q75),item_at(8,WL4,Q85),
		retract(workList([Q15,Q25,Q35,Q45,Q55,Q65,Q75,Q85])), asserta(workList([Q15,Q25,Q35,Q45,Fo5,Q65,Q75,Q85])) 
	; !
	),
	(S6 = 'R'
	->	workList(WL5), index(Id6), item_at(Id6,Item,Fo6),retract(index(Id6)), Newid6 is Id6+1, asserta(index(Newid6)),
		item_at(1,WL5,Q16),item_at(2,WL5,Q26),item_at(3,WL5,Q36),item_at(4,WL5,Q46),
		item_at(5,WL5,Q56),item_at(6,WL5,Q66),item_at(7,WL5,Q76),item_at(8,WL5,Q86),
		retract(workList([Q16,Q26,Q36,Q46,Q56,Q66,Q76,Q86])), asserta(workList([Q16,Q26,Q36,Q46,Q56,Fo6,Q76,Q86])) 
	; !
	),
	(S7 = 'R'
	->	workList(WL6), index(Id7), item_at(Id7,Item,Fo7),retract(index(Id7)), Newid7 is Id7+1, asserta(index(Newid7)),
		item_at(1,WL6,Q17),item_at(2,WL6,Q27),item_at(3,WL6,Q37),item_at(4,WL6,Q47),
		item_at(5,WL6,Q57),item_at(6,WL6,Q67),item_at(7,WL6,Q77),item_at(8,WL6,Q87),
		retract(workList([Q17,Q27,Q37,Q47,Q57,Q67,Q77,Q87])), asserta(workList([Q17,Q27,Q37,Q47,Q57,Q67,Fo7,Q87])) 
	; !
	),
	(S8 = 'R'
	->	workList(WL7), index(Id8), item_at(Id8,Item,Fo8),retract(index(Id8)), Newid8 is Id8+1, asserta(index(Newid8)),
		item_at(1,WL7,Q18),item_at(2,WL7,Q28),item_at(3,WL7,Q38),item_at(4,WL7,Q48),
		item_at(5,WL7,Q58),item_at(6,WL7,Q68),item_at(7,WL7,Q78),item_at(8,WL7,Q88),
		retract(workList([Q18,Q28,Q38,Q48,Q58,Q68,Q78,Q88])), asserta(workList([Q18,Q28,Q38,Q48,Q58,Q68,Q78,Fo8])) 
	; !
	).
	
softcon(Item):-
	Z is 0,
	X is 0,
	%write(Item),%nl,
	%write('SOG'),%nl,
	tnps(Z,Item),
	mach(Item, X),
	tempval(XX),
	sum(ZZ), 
	S is XX + ZZ,
	retract(tempval(XX)),
	retract(sum(ZZ)),
	bestPen(B),
	bestSol(G),
	(S < B
	-> retract(bestPen(B)), asserta(bestPen(S)), retract(bestSol(G)), asserta(bestSol(Item))
	; !
	),
	bestPen(AAA),
	(AAA = 0
	-> pvs
	; !
	).

tnps(Z,Item):-
    asserta(sum(Z)),
    item_at(1,Item,A),
    item_at(2,Item,B),
    (tnp(['(',A,',',B,',',C,')'])
     -> valcon(C,RR),sum(PP), retract(sum(PP)), TT is PP + RR, asserta(sum(TT))
     ; !
     ),
    item_at(2,Item,A1),
    item_at(3,Item,B1),
    (tnp(['(',A1,',',B1,',',C1,')'])
     -> valcon(C1,RR1),sum(PP1), retract(sum(PP1)), TT1 is PP1 + RR1, asserta(sum(TT1))
     ; !
     ),
    item_at(3,Item,A2),
    item_at(4,Item,B2),
    (tnp(['(',A2,',',B2,',',C2,')'])
     -> valcon(C2,RR2),sum(PP2), retract(sum(PP2)), TT2 is PP2 + RR2, asserta(sum(TT2))
     ; !
     ),
    item_at(4,Item,A3),
    item_at(5,Item,B3),
    (tnp(['(',A3,',',B3,',',C3,')'])
     -> valcon(C3,RR3),sum(PP3), retract(sum(PP3)), TT3 is PP3 + RR3, asserta(sum(TT3))
     ; !
     ),
    item_at(5,Item,A4),
    item_at(6,Item,B4),
    (tnp(['(',A4,',',B4,',',C4,')'])
     -> valcon(C4,RR4), sum(PP4), retract(sum(PP4)),TT4 is PP4 + RR4, asserta(sum(TT4))
     ; !
     ),
    item_at(6,Item,A5),
    item_at(7,Item,B5),
    (tnp(['(',A5,',',B5,',',C5,')'])
     -> valcon(C5,RR5), sum(PP5), retract(sum(PP5)),TT5 is PP5 + RR5, asserta(sum(TT5))
     ; !
     ),
    item_at(7,Item,A6),
    item_at(8,Item,B6),
    (tnp(['(',A6,',',B6,',',C6,')'])
     -> valcon(C6,RR6), sum(PP6), retract(sum(PP6)),TT6 is PP6 + RR6, asserta(sum(TT6))
     ; !
     ),
    item_at(8,Item,A7),
    item_at(1,Item,B7),
    (tnp(['(',A7,',',B7,',',C7,')'])
     -> valcon(C7,RR7), sum(PP7), retract(sum(PP7)),TT7 is PP7 + RR7, asserta(sum(TT7))
     ; !
     ).

mach(SOL, X):-
	item_at(1,SOL,A),
	valcon(A,C),
	mp0(L),
	item_at(C,L,P),
	T is X + P,

	item_at(2,SOL,A2),
	valcon(A2,C2),
	mp1(L2),
	item_at(C2,L2,P2),
	O is T + P2,

	item_at(3,SOL,A3),
	valcon(A3,C3),
	mp2(L3),
	item_at(C3,L3,P3),
	K is O + P3,

	item_at(4,SOL,A4),
	valcon(A4,C4),
	mp3(L4),
	item_at(C4,L4,P4),
	B is K + P4,

	item_at(5,SOL,A5),
	valcon(A5,C5),
	mp4(L5),
	item_at(C5,L5,P5),
	D is B + P5,

	item_at(6,SOL,A6),
	valcon(A6,C6),
	mp5(L6),
	item_at(C6,L6,P6),
	J is D + P6,

	item_at(7,SOL,A7),
	valcon(A7,C7),
	mp6(L7),
	item_at(C7,L7,P7),
	S is J + P7,

	item_at(8,SOL,A8),
	valcon(A8,C8),
	mp7(L8),
	item_at(C8,L8,P8),
	JJ is S + P8,
	asserta(tempval(JJ)).

    tnts(Item):-
    item_at(1,Item,A),
    item_at(2,Item,B),
    (tnt(['(',A,',',B,')'])
     -> fail
     ;item_at(2,Item,A1),
     item_at(3,Item,B1),
     (tnt(['(',A1,',',B1,')'])
      -> fail
      ;item_at(3,Item,A2),
      item_at(4,Item,B2),
      (tnt(['(',A2,',',B2,')'])
       -> fail
       ;item_at(4,Item,A3),
       item_at(5,Item,B3),
       (tnt(['(',A3,',',B3,')'])
	-> fail
	;item_at(5,Item,A4),
	item_at(6,Item,B4),
	(tnt(['(',A4,',',B4,')'])
	 -> fail
	 ;item_at(6,Item,A5),
	 item_at(7,Item,B5),
	 (tnt(['(',A5,',',B5,')'])
	  -> fail
	  ;item_at(7,Item,A6),
	  item_at(8,Item,B6),
	  (tnt(['(',A6,',',B6,')'])
	   -> fail
	   ;item_at(8,Item,A7),
	   item_at(1,Item,B7),
	   (tnt(['(',A7,',',B7,')'])
	    -> fail
	    ; !
	   )
	  )
	 )
	)
       )
      )
     )
    ).

    fms(Item):-
    item_at(1,Item,B),
    (fm(['(','1',',',B,')'])
     -> fail
     ;item_at(2,Item,B1),
     (fm(['(','2',',',B1,')'])
      -> fail
      ;item_at(3,Item,B2),
      (fm(['(','3',',',B2,')'])
       -> fail
       ;item_at(4,Item,B3),
       (fm(['(','4',',',B3,')'])
	-> fail
	;item_at(5,Item,B4),
	(fm(['(','5',',',B4,')'])
	 -> fail
	 ;item_at(6,Item,B5),
	 (fm(['(','6',',',B5,')'])
	  -> fail
	  ;item_at(7,Item,B6),
	  (fm(['(','7',',',B6,')'])
	   -> fail
	   ;item_at(8,Item,B7),
	   (fm(['(','8',',',B7,')'])
	    -> fail
	    ; !
	   )
	  )
	 )
	)
       )
      )
     )
    ).

	
    fpas(Item):-
    item_at(1,Item,B),
    (fpam('1'),fpat(B)
     ->(fpa(['(','1',',',B,')'])%rite(I),nl
	-> !
	; fail
     )
     ;(fpam('1');fpat(B)
       -> fail
       ; !
     )
    ),
    item_at(2,Item,B2),
    (fpam('2'),fpat(B2)
     ->(fpa(['(','2',',',B2,')'])
	-> !
	; fail
     )
     ;(fpam('2');fpat(B2)
       -> fail
       ; !
     )
    ),
    item_at(3,Item,B3),
    (fpam('3'),fpat(B3)
     ->(fpa(['(','3',',',B3,')'])
	-> !
	; fail
     )
     ;(fpam('3');fpat(B3)
       -> fail
       ; !
     )
    ),
    item_at(4,Item,B4),
    (fpam('4'),fpat(B4)
     ->(fpa(['(','4',',',B4,')'])
	-> !
	; fail
     )
     ;(fpam('4');fpat(B4)
       -> fail
       ; !
     )
    ),
    item_at(5,Item,B5),
    (fpam('5'),fpat(B5)
     ->(fpa(['(','5',',',B5,')'])
	-> !
	; fail
     )
     ;(fpam('5');fpat(5)
       -> fail
       ; !
     )
    ),
    item_at(6,Item,B6),
    (fpam('6'),fpat(B6)
     ->(fpa(['(','6',',',B6,')'])
	-> !
	; fail
     )
     ;(fpam('6');fpat(B6)
       -> fail
       ; !
     )
    ),
    item_at(7,Item,B7),
    (fpam('7'),fpat(B7)
     ->(fpa(['(','7',',',B7,')'])
	-> !
	; fail
     )
     ;(fpam('7');fpat(B7)
       -> fail
       ; !
     )
    ),
    item_at(8,Item,B8),
    (fpam('8'),fpat(B8)
     ->(fpa(['(','8',',',B8,')'])
	-> !
	; fail
     )
     ;(fpam('8');fpat(B8)
       -> fail
       ; !
     )
    ).


    readWord(InStream,W):-
    get_code(InStream,Char),
    checkCharAndReadRest(Char,Chars,InStream),
    atom_codes(W,Chars).    
    
    checkCharAndReadRest(10,[],_):-  !.

    checkCharAndReadRest(-1,[97],_):-  !.    

    checkCharAndReadRest(end_of_file,[97],_):-  !.
    
    checkCharAndReadRest(32,Chars,InStream):-
    get_code(InStream,NextChar),
    checkCharAndReadRest(NextChar, Chars,InStream).

    checkCharAndReadRest(9,Chars,InStream):-
    get_code(InStream,NextChar),
    checkCharAndReadRest(NextChar, Chars,InStream).

    checkCharAndReadRest(11,Chars,InStream):-
    get_code(InStream,NextChar),
    checkCharAndReadRest(NextChar, Chars,InStream).

    checkCharAndReadRest(12,Chars,InStream):-
    get_code(InStream,NextChar),
    checkCharAndReadRest(NextChar, Chars,InStream).

    checkCharAndReadRest(13,Chars,InStream):-
    get_code(InStream,NextChar),
    checkCharAndReadRest(NextChar, Chars,InStream).

    checkCharAndReadRest(14,Chars,InStream):-
    get_code(InStream,NextChar),
    checkCharAndReadRest(NextChar, Chars,InStream).

    checkCharAndReadRest(Char,[Char|Chars],InStream):-
    get_code(InStream,NextChar),
    checkCharAndReadRest(NextChar,Chars,InStream). 



    readWordMp(InStream,W):-
    get_code(InStream,Char),
    checkCharAndReadRestMp(Char,Chars,InStream),
    atom_codes(W,Chars).    
    
    checkCharAndReadRestMp(10,[],_):-  !.

    checkCharAndReadRestMp(-1,[97],_):-  !.    

    checkCharAndReadRestMp(end_of_file,[97],_):-  !.
    
    /*checkCharAndReadRest(32,Chars,InStream):-
    get_code(InStream,NextChar),
    checkCharAndReadRest(NextChar, Chars,InStream).*/

    checkCharAndReadRestMp(9,Chars,InStream):-
    get_code(InStream,NextChar),
    checkCharAndReadRestMp(NextChar, Chars,InStream).

    checkCharAndReadRestMp(11,Chars,InStream):-
    get_code(InStream,NextChar),
    checkCharAndReadRestMp(NextChar, Chars,InStream).

    checkCharAndReadRestMp(12,Chars,InStream):-
    get_code(InStream,NextChar),
    checkCharAndReadRestMp(NextChar, Chars,InStream).

    checkCharAndReadRestMp(13,Chars,InStream):-
    get_code(InStream,NextChar),
    checkCharAndReadRestMp(NextChar, Chars,InStream).

    checkCharAndReadRestMp(14,Chars,InStream):-
    get_code(InStream,NextChar),
    checkCharAndReadRestMp(NextChar, Chars,InStream).

    checkCharAndReadRestMp(Char,[Char|Chars],InStream):-
    get_code(InStream,NextChar),
    checkCharAndReadRestMp(NextChar,Chars,InStream).

    checkHeaders :- 
    header(name),
    header(fpa),
    header(fm),
    header(tnt),
    header(mp),
    header(tnp).

    item_at( N, L, Item ) :-
    item_at( N, 0, L, Item ).   
    item_at( N, Count, [H|_], Item ) :-
    CountNew is Count + 1,
    CountNew = N,
    Item = H.
    item_at( N, Count, [_|T], Item ) :-
    CountNew is Count + 1,
    item_at( N, CountNew, T, Item ).

    half(L, Po, Pt):- half(L,L,Po,Pt), !.
    half(Po,[],[],Po).
    half(Po, [_],[],Po).
    half([S|E], [_,_|E2],[S,Po],Pt) :-half(E,E2,Po,Pt).

    validtask('(A,A)').    
    validtask('(B,A)').    
    validtask('(C,A)').    
    validtask('(D,A)').    
    validtask('(E,A)').    
    validtask('(F,A)').    
    validtask('(G,A)').    
    validtask('(H,A)').    
    validtask('(A,B)').    
    validtask('(B,B)').    
    validtask('(C,B)').    
    validtask('(D,B)').    
    validtask('(E,B)').    
    validtask('(F,B)').    
    validtask('(G,B)').    
    validtask('(H,B)').    
    validtask('(A,C)').    
    validtask('(B,C)').    
    validtask('(C,C)').    
    validtask('(D,C)').    
    validtask('(E,C)').    
    validtask('(F,C)').    
    validtask('(G,C)').    
    validtask('(H,C)').    
    validtask('(A,D)').    
    validtask('(B,D)').    
    validtask('(C,D)').    
    validtask('(D,D)').    
    validtask('(E,D)').    
    validtask('(F,D)').    
    validtask('(G,D)').    
    validtask('(H,D)').    
    validtask('(A,E)').    
    validtask('(B,E)').    
    validtask('(C,E)').    
    validtask('(D,E)').    
    validtask('(E,E)').    
    validtask('(F,E)').    
    validtask('(G,E)').    
    validtask('(H,E)').    
    validtask('(A,F)').    
    validtask('(B,F)').    
    validtask('(C,F)').    
    validtask('(D,F)').    
    validtask('(E,F)').    
    validtask('(F,F)').    
    validtask('(G,F)').    
    validtask('(H,F)').    
    validtask('(A,G)').    
    validtask('(B,G)').    
    validtask('(C,G)').    
    validtask('(D,G)').    
    validtask('(E,G)').    
    validtask('(F,G)').    
    validtask('(G,G)').    
    validtask('(H,G)').    
    validtask('(A,H)').    
    validtask('(B,H)').    
    validtask('(C,H)').    
    validtask('(D,H)').    
    validtask('(E,H)').    
    validtask('(F,H)').    
    validtask('(G,H)').    
    validtask('(H,H)').    


    validpair('(1,A)').
    validpair('(2,A)').
    validpair('(3,A)').
    validpair('(4,A)').
    validpair('(5,A)').
    validpair('(6,A)').
    validpair('(7,A)').
    validpair('(8,A)').
    validpair('(1,B)').
    validpair('(2,B)').
    validpair('(3,B)').
    validpair('(4,B)').
    validpair('(5,B)').
    validpair('(6,B)').
    validpair('(7,B)').
    validpair('(8,B)').
    validpair('(1,C)').
    validpair('(2,C)').
    validpair('(3,C)').
    validpair('(4,C)').
    validpair('(5,C)').
    validpair('(6,C)').
    validpair('(7,C)').
    validpair('(8,C)').
    validpair('(1,D)').
    validpair('(2,D)').
    validpair('(3,D)').
    validpair('(4,D)').
    validpair('(5,D)').
    validpair('(6,D)').
    validpair('(7,D)').
    validpair('(8,D)').
    validpair('(1,E)').
    validpair('(2,E)').
    validpair('(3,E)').
    validpair('(4,E)').
    validpair('(5,E)').
    validpair('(6,E)').
    validpair('(7,E)').
    validpair('(8,E)').
    validpair('(1,F)').
    validpair('(2,F)').
    validpair('(3,F)').
    validpair('(4,F)').
    validpair('(5,F)').
    validpair('(6,F)').
    validpair('(7,F)').
    validpair('(8,F)').
    validpair('(1,G)').
    validpair('(2,G)').
    validpair('(3,G)').
    validpair('(4,G)').
    validpair('(5,G)').
    validpair('(6,G)').
    validpair('(7,G)').
    validpair('(8,G)').
    validpair('(1,H)').
    validpair('(2,H)').
    validpair('(3,H)').
    validpair('(4,H)').
    validpair('(5,H)').
    validpair('(6,H)').
    validpair('(7,H)').
    validpair('(8,H)').

    bestPen(999999).
    bestSol(['A','B','C','D','E','F','G','H']).
	
    exists('A').
    exists('B').
    exists('C').
    exists('D').
    exists('E').
    exists('F').
    exists('G').
    exists('H').

	utask('A').
	utask('B').
	utask('C').
	utask('D').
	utask('E').
	utask('F').
	utask('G').
	utask('H').
	
	fpaSol(['R','R','R','R','R','R','R','R']).
	permSize(8).
	workList(['Z','Z','Z','Z','Z','Z','Z','Z']).
	index(1).
	
    valcon('A',1).
    valcon('B',2).
    valcon('C',3).
    valcon('D',4).
    valcon('E',5).
    valcon('F',6).
    valcon('G',7).
    valcon('H',8).
    valcon('0',0).
    valcon('1',1).
    valcon('2',2).
    valcon('3',3).
    valcon('4',4).
    valcon('5',5).
    valcon('6',6).
    valcon('7',7).
    valcon('8',8).
    valcon('9',9).
    penexists('0').
    penexists('1').
    penexists('2').
    penexists('3').
    penexists('4').
    penexists('5').
    penexists('6').
    penexists('7').
    penexists('8').
    penexists('9').
	fmcount('1',0).
	fmcount('2',0).
	fmcount('3',0).
	fmcount('4',0).
	fmcount('5',0).
	fmcount('6',0).
	fmcount('7',0).
	fmcount('8',0).
	fmcount('A',0).
	fmcount('B',0).
	fmcount('C',0).
	fmcount('D',0).
	fmcount('E',0).
	fmcount('F',0).
	fmcount('G',0).
	fmcount('H',0).

	taskList([['Z']]).
    /*number_codes(N,"")*/
    
