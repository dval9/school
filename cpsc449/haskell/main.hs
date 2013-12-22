module Main where
import Data.List
import System.IO
import System.Environment (getArgs)
import System.Exit
import Control.Monad (liftM)
import Data.Char

type MachTask = (Char, Char) --defining type for tuple (mach,task)
type TaskTask = (Char, Char) --defining type for tuple (task, task)
type TaskTaskPen = (Char, Char, Int) --defining type for tuple (task, task, pen)
type ForcedPartialAssignment = [MachTask] --defining list that will hold forced partial assignment
type ForbiddenMachine = [MachTask] --defining list that will hold forbidden machine assignment
type TooNearTasks = [TaskTask] --defining list to hold too-near tasks
type MachinePenalties = [[Int]] --defining list to hold 8 rows of penalties
type TooNearPenalities = [TaskTaskPen] --defining list to hold too-near penalities, note weird spelling
type PossibleSolutions = [[MachTask]] --defining list for solutions
type BestSolution = [MachTask] --defining list for final solution


copyelem qty item = [item|_<-[1..qty]]

perm            :: [a] -> [[a]]
perm xs0        =  xs0 : perms xs0 []
  where
    perms []     _  = []
    perms (t:ts) is = foldr interleave (perms ts (t:is)) (perm is)
      where interleave    xs     r = let (_,zs) = interleave' id xs r in zs
            interleave' _ []     r = (ts, r)
            interleave' f (y:ys) r = let (us,zs) = interleave' (f . (y:)) ys r
                                     in  (y:us, f (t:y:us) : zs)

--Given MachTask tuple, will return first element of tuple
getMachTask1 :: MachTask -> Char
getMachTask1 (a, b) = a

--Given MachTask tuple, will return second element of tuple
getMachTask2 :: MachTask -> Char
getMachTask2 (a, b) = b

--Given TaskTask tuple, will return first element of tuple
getTaskTask1 :: TaskTask -> Char
getTaskTask1 (a, b) = a

--Given TaskTask tuple, will return second element of tuple
getTaskTask2 :: TaskTask -> Char
getTaskTask2 (a, b) = b

--Can use these functions to access element of the TaskTaskPen tuple
--Get first element in tuple
getTaskTaskPen1 :: TaskTaskPen -> Char
getTaskTaskPen1 (a, b, c) = a
--Get second element in tuple
getTaskTaskPen2 :: TaskTaskPen -> Char
getTaskTaskPen2 (a, b, c) = b
--Get third element in tuple
getTaskTaskPen3 :: TaskTaskPen -> Int
getTaskTaskPen3 (a, b, c) = c

gen :: [[Char]] -> [Char] -> [[(Char, Char)]] -> [[(Char, Char)]]
gen permtask mach tot
		| permtask == [] = tot
		| otherwise = gen (drop 1 permtask) mach ((zip mach (head permtask)) : tot)
		
main :: IO ()

solutions1 = [] :: PossibleSolutions
solutions=[[]]
tempsolution = [] :: BestSolution
machs = ['1'..'8']
tasks = ['A'..'H']
machtasktuples=[]
paeerr = "partial assignment error"
imterr = "invalid machine/task"
mpeerr = "machine penalty error"
ivterr = "invalid task"
ivperr = "invalid penalty"
piferr = "Error while parsing input file"
novsol = "No valid solution possible!"
solstr = "Solution "  
qalstr = "; Quality: " 

fst3 :: (Char,Char,Int) -> Char
fst3 (a,b,c) = a

snd3 :: (Char,Char,Int) -> Char
snd3 (a,b,c) = b

thr3 :: (Char,Char,Int) -> Int
thr3 (a,b,c) = c

trd3 (a,b,c) = c

special :: [(Char,Char)] -> [(Char,Char,Int)] -> Int -> Int
special sol ttp totpen
	| ttp == [] = totpen
	| otherwise = if (((snd (last sol)) == (fst3 (head ttp))) && ((snd(head sol)) == (snd3 (head ttp))))
						then special sol [] (totpen+(thr3 (head ttp)))
						else special sol (drop 1 ttp) totpen

						
toonearpen :: [(Char,Char)] -> [(Char,Char,Int)] -> Int  -> [(Char,Char,Int)] -> Int
toonearpen sol ttp totpen toonearpens
	| (length sol) == 1 = totpen
	| ttp == [] = toonearpen (drop 1 sol) (toonearpens) totpen toonearpens
	| otherwise = if (((snd(head sol)) == (fst3 (head ttp))) && ((snd(head (drop 1 sol))) == (snd3 (head ttp))))
						then toonearpen (drop 1 sol) toonearpens (totpen+(thr3 (head ttp))) toonearpens
						else toonearpen sol	(drop 1 ttp) totpen toonearpens

hardspecial :: [(Char,Char)] -> [(Char,Char)] -> Bool
hardspecial sol tt
	| tt == [] = False
	| otherwise = if (((snd (last sol)) == (fst (head tt))) && ((snd(head sol)) == (snd (head tt))))
						then True
						else hardspecial sol (drop 1 tt) 

						
hardtoonearpen :: [(Char,Char)] -> [(Char,Char)]  -> [(Char,Char)] -> Bool
hardtoonearpen sol tt tooneartasks
	| (length sol) == 1 = False
	| tt == [] = hardtoonearpen (drop 1 sol) (tooneartasks) tooneartasks
	| otherwise = if (((snd(head sol)) == (fst (head tt))) && ((snd(head (drop 1 sol))) == (snd (head tt))))
						then True
						else hardtoonearpen sol	(drop 1 tt) tooneartasks

chartoint :: Char -> Int
chartoint x
	| ((x == 'A') || (x == '1')) = 0
	| ((x == 'B') || (x == '2')) = 1
	| ((x == 'C') || (x == '3')) = 2
	| ((x == 'D') || (x == '4')) = 3
	| ((x == 'E') || (x == '5')) = 4
	| ((x == 'F') || (x == '6')) = 5
	| ((x == 'G') || (x == '7')) = 6
	| ((x == 'H') || (x == '8')) = 7

		

asspen :: [(Char,Char)] -> Int -> [[Int]] ->Int
asspen sol totpen machpens
		| sol == [] = totpen
		| otherwise = asspen (drop 1 sol)(((machpens!!(chartoint(fst (head sol))))!!(chartoint(snd (head sol)))) + totpen) machpens

checkdubs :: [Char] -> [Char]-> Bool
checkdubs input base
			| input == [] = False
			| (elem (head input) base) = True
			| otherwise = checkdubs (drop 1 input) (drop 1 base)




softcon :: [(Char,Char)] -> [[Int]] -> [(Char,Char,Int)] -> Int --takes as input a list of (Char, Char) tuples and outputs and int (penalty)
softcon sol machpens toonearpens
	    | (((asspen sol 0 machpens) > 0) && ((toonearpen sol toonearpens 0 toonearpens) > 0) && ((special sol toonearpens 0) > 0)) = ((asspen sol 0 machpens) + (toonearpen sol toonearpens 0 toonearpens) + (special sol toonearpens 0)) --if the assignment penalty > 0 and too near penalty > 0 then its equal to the sum
		| (((asspen sol 0 machpens) > 0) && ((toonearpen sol toonearpens 0 toonearpens) > 0)) = ((asspen sol 0 machpens) + (toonearpen sol toonearpens 0 toonearpens))
		| (((asspen sol 0 machpens) > 0) && ((special sol toonearpens 0) > 0)) = ((asspen sol 0 machpens) + (special sol toonearpens 0))
		| (((toonearpen sol toonearpens 0 toonearpens) > 0)  && ((special sol toonearpens 0) > 0)) = ((special sol toonearpens 0) + (toonearpen sol toonearpens 0 toonearpens))
		| ((special sol toonearpens 0) > 0) = special sol toonearpens 0
		| ((asspen sol 0 machpens) > 0) = asspen sol 0 machpens -- if the assignment pnalty is greater than 0 then it is the assignment pentaly
		| ((toonearpen sol toonearpens 0 toonearpens) > 0) = toonearpen sol toonearpens 0 toonearpens -- if the toonear penalty is greater than 0 it is the too near penalty
		| otherwise = 0 -- otherwise the penalty is 0

try :: [[(Char, Char)]] -> [(Char, Char)] -> [[Int]] -> [(Char, Char, Int)] -> [(Char, Char)] 	-- take as input a list of machines, a list of a list of tasks, and out best task, output the new best task
try tm best	machpens toonearpens
	| tm == [] = best					-- if input is empty then we start to rewind
	| best == [] = try (drop 1 tm) (head tm) machpens toonearpens	--base case
	| otherwise = if((softcon best machpens toonearpens) > (softcon (head tm) machpens toonearpens))
											then try (drop 1 tm) (head tm) machpens toonearpens
											else try (drop 1 tm) best machpens toonearpens

											
hardcon :: [[(Char,Char)]] ->[[(Char,Char)]]-> [(Char,Char)] -> [(Char,Char)] -> [(Char,Char)] -> [[Int]] -> [(Char, Char, Int)] -> [[(Char,Char)]]
hardcon tot sol forced forbidden tooneartasks machpens toonearpens
			| tot == [] = sol
			| length [(a, b) | (a, b) <- (head tot), not $ any (\(c, d) -> (a == c && b /= d) || (a /= c && b == d)) forced] /= 8 = hardcon (drop 1 tot) sol forced forbidden tooneartasks machpens toonearpens
			| ((length ((head tot)\\forbidden)) /= 8) = hardcon (drop 1 tot) sol forced forbidden tooneartasks machpens toonearpens
			| hardspecial (head tot) tooneartasks == True = hardcon (drop 1 tot) sol forced forbidden tooneartasks machpens toonearpens
			| hardtoonearpen (head tot) tooneartasks tooneartasks == True = hardcon (drop 1 tot) sol forced forbidden tooneartasks machpens toonearpens
			|otherwise =  hardcon (drop 1 tot) ((head tot) : sol) forced forbidden tooneartasks machpens toonearpens

readname input outfile
				| ((head input) == "") = readname (drop 1 input) outfile
				| ((head input) == "Name:") = readname1 (drop 1 input) outfile
				| otherwise = do 
										hPutStrLn outfile piferr
										hClose outfile
										exitFailure

readname1 input outfile
				| ((head input) == "") = readname1 (drop 1 input) outfile
				| ((head input) /= "forced partial assignment:" && (head input) /= "") = readname2 (drop 1 input) outfile
				| otherwise = do 
										hPutStrLn outfile piferr
										hClose outfile
										exitFailure

readname2 input outfile
				| ((head input) == "") = readname2 (drop 1 input) outfile
				| ((head input) == "forced partial assignment:") = return ()
				| otherwise = do 
										hPutStrLn outfile piferr
										hClose outfile
										exitFailure

readforced input
				| ((head input) /= "forced partial assignment:") = readforced (drop 1 input)
				| ((head input) == "forced partial assignment:") = readforced1 (drop 1 input) []

readforced1 input x
				| ((head input) == "") = readforced1 (drop 1 input) x
				| ((head input) == "forbidden machine:") = x
				| otherwise = readforced1 (drop 1 input) (zip [((head input)!!1)] [((head input)!!3)] : x)

readforbidden input
				| (input == []) = [[('Z','1')]]
				| ((head input) /= "forbidden machine:") = readforbidden (drop 1 input)
				| ((head input) == "forbidden machine:") = readforbidden1 (drop 1 input) []

readforbidden1 input x
				| ((head input) == "") = readforbidden1 (drop 1 input) x
				| ((head input) == "too-near tasks:") = x
				| otherwise = readforbidden1 (drop 1 input) (zip [((head input)!!1)] [((head input)!!3)] : x)

readtooneartasks input
				| ((head input) /= "too-near tasks:") = readtooneartasks (drop 1 input)
				| ((head input) == "too-near tasks:") = readtooneartasks1 (drop 1 input) []

readtooneartasks1 input x
				| ((head input) == "") = readtooneartasks1 (drop 1 input) x
				| ((head input) == "machine penalties:") = x
				| otherwise = readtooneartasks1 (drop 1 input) (zip [((head input)!!1)] [((head input)!!3)] : x)

readmachinepenalties input
				| ((head input) /= "machine penalties:") = readmachinepenalties (drop 1 input)
				| ((head input) == "machine penalties:") = readmachinepenalties1 (drop 1 input) []

readmachinepenalties1 input x
				| ((head input) == "") = readmachinepenalties1 (drop 1 input) x
				| ((head input) == "too-near penalities") = x
				| otherwise = readmachinepenalties1 (drop 1 input) ((map read $ words (head input) :: [Int]) : x)

chkmachinepenalties input
				| ((head input) /= "machine penalties:") = chkmachinepenalties (drop 1 input)
				| ((head input) == "machine penalties:") = chkmachinepenalties1 (drop 1 input)

chkmachinepenalties1 input 
				| ((head input) == "") = chkmachinepenalties1 (drop 1 input)
				| ((head input) == "too-near penalities") = True
				| (isTrues (map isInteger (words (head input)))) = chkmachinepenalties1 (drop 1 input)
				| otherwise = False

chktoonear input
				| ((head input) /= "too-near penalities") = chktoonear (drop 1 input)
				| ((head input) == "too-near penalities") = chktoonear1 (drop 1 input)

chktoonear1 input 
				| (input == []) = True
				| ((head input) == "") = chktoonear1 (drop 1 input)
				| (isInteger [((head input)!!5)]) = chktoonear1 (drop 1 input)
				| otherwise = False

readtoonearpenalities input
				| ((head input) /= "too-near penalities") = readtoonearpenalities (drop 1 input)
				| ((head input) == "too-near penalities") = readtoonearpenalities1 (drop 1 input) []

readtoonearpenalities1 input x
				| (input == []) = x
				| ((head input) == "") = readtoonearpenalities1 (drop 1 input) x
				| otherwise = readtoonearpenalities1 (drop 1 input) (zip3 [((head input)!!1)] [((head input)!!3)] [(digitToInt ((head input)!!5))] : x)

readheaders input
				| ((elem "Name:" input) && (elem "forced partial assignment:" input) && (elem "forbidden machine:" input) && (elem "too-near tasks:" input) && (elem "machine penalties:" input) && (elem "too-near penalities" input)) = True
				| otherwise = False

trim :: String -> String
trim = f . f
    where f = reverse . dropWhile isSpace

isInteger s = case reads s :: [(Integer, String)] of
	[(_, "")] -> True
	_ -> False

isTrues :: [Bool] -> Bool
isTrues x
		| (x == []) = True
		| ((head x) == False)= False
		| otherwise = isTrues (drop 1 x)

isCharValid :: [Char] -> Bool
isCharValid x
		| (x == []) = True
		| (elem (head x) tasks) = isCharValid (drop 1 x)
		| otherwise = False

isCharValid1 :: [Char] -> Bool
isCharValid1 x
		| (x == []) = True
		| (elem (head x) machs) = isCharValid1 (drop 1 x)
		| otherwise = False

isnegs :: [[Int]] -> Bool
isnegs arr
		| (arr == []) = False
		| ((isnegs1 (head arr)) == True) = True
		| otherwise = isnegs (drop 1 arr)

isnegs1 :: [Int] -> Bool
isnegs1 arr
		| (arr == []) = False
		| (head arr) < 0 = True
		| otherwise = isnegs1 (drop 1 arr)

main = do			
	args <- getArgs
	if (length args /= 2)
	then do 
		putStrLn "wrong args\n"
		exitFailure
	else
		return ()
	infile <- openFile (args!!0) ReadMode --change to (args !! 0)
	outfile <- openFile (args!!1) WriteMode --change to (args !! 1)
	--start parsing input
	filecontents <- hGetContents infile
	let contents = lines filecontents 	
	let inputstuff = map trim contents
	if(readheaders inputstuff)
	then return ()
	else do
		hPutStrLn outfile piferr
		hClose outfile
		exitFailure
	readname inputstuff outfile
	let forced = map head (readforced inputstuff)
	if(isCharValid1 (map fst forced))
	then return ()
	else do
		hPutStrLn outfile imterr
		hClose outfile
		exitFailure
	if(isCharValid (map snd forced))
	then return ()
	else do
		hPutStrLn outfile imterr
		hClose outfile
		exitFailure
	if (checkdubs (map snd forced) (drop 1 (map snd forced)))
	then do
		hPutStrLn outfile paeerr
		hClose outfile
		exitFailure
	else return()
	let forbidden = map head (readforbidden inputstuff)
	if(isCharValid (map snd forbidden))
	then return ()
	else do
		hPutStrLn outfile imterr
		hClose outfile
		exitFailure
	if(isCharValid1 (map fst forbidden))
	then return ()
	else do
		hPutStrLn outfile imterr
		hClose outfile
		exitFailure
	if((forbidden /= []) && (fst(head forbidden) == 'Z'))
	then do
		hPutStrLn outfile piferr
		hClose outfile
		exitFailure
	else
		return ()
	let tooneartasks = map head (readtooneartasks inputstuff)
	if(isCharValid (map fst tooneartasks))
	then return ()
	else do
		hPutStrLn outfile imterr
		hClose outfile
		exitFailure
	if(isCharValid (map snd tooneartasks))
	then return ()
	else do
		hPutStrLn outfile imterr
		hClose outfile
		exitFailure
	if(chkmachinepenalties inputstuff)
	then return()
	else do
		hPutStrLn outfile ivperr
		hClose outfile
		exitFailure
	let machpens = reverse $ readmachinepenalties inputstuff
	if(((length machpens) /= 8) || (length (machpens!!0) /= 8) || (length (machpens!!1) /= 8) || (length (machpens!!2) /= 8) || (length (machpens!!3) /= 8) || (length (machpens!!4) /= 8) || (length (machpens!!5) /= 8) || (length (machpens!!6) /= 8) || (length (machpens!!7) /= 8))
	then do
		hPutStrLn outfile mpeerr
		hClose outfile
		exitFailure
	else
		return ()
	if(isnegs machpens)
	then do
		hPutStrLn outfile ivperr
		hClose outfile
		exitFailure
	else 
		return()
	let toonearpens = map head (readtoonearpenalities inputstuff)
	if (chktoonear inputstuff)
	then return ()
	else do
		hPutStrLn outfile ivperr
		hClose outfile
		exitFailure		
	if(isCharValid (map snd3 toonearpens))
	then return ()
	else do
		hPutStrLn outfile ivterr
		hClose outfile
		exitFailure
	if(isCharValid (map fst3 toonearpens))
	then return ()
	else do
		hPutStrLn outfile ivterr
		hClose outfile
		exitFailure
	--do work       
	let solutions1=gen (perm tasks) machs []
	let solutions2=try (hardcon solutions1 [] forced forbidden tooneartasks machpens toonearpens) [] machpens toonearpens
	let penalty=softcon solutions2 machpens toonearpens
	--end doing work  
	if (length solutions2) == 8 
	then hPutStrLn outfile $ solstr ++ ((map getMachTask2 solutions2) !! 0 : " ")
									++ ((map getMachTask2 solutions2) !! 1 : " ")
									++ ((map getMachTask2 solutions2) !! 2 : " ")
									++ ((map getMachTask2 solutions2) !! 3 : " ")
									++ ((map getMachTask2 solutions2) !! 4 : " ")
									++ ((map getMachTask2 solutions2) !! 5 : " ")
									++ ((map getMachTask2 solutions2) !! 6 : " ")
									++ ((map getMachTask2 solutions2) !! 7 : "")
									++ qalstr
									++ (show penalty)
	else hPutStrLn outfile novsol
	hClose infile
	hClose outfile 

























