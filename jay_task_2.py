#!/usr/bin/env python2
# -*- coding: utf-8 -*-
"""
Created on Fri Jun  8 13:37:35 2018

@author: kaushik
"""

from nltk.tokenize import RegexpTokenizer

from nltk.stem.porter import PorterStemmer
from gensim import corpora
import pandas as pd
import warnings
#warnings.filterwarnings(action='ignore', category=UserWarning, module='gensim')
import gensim
import json
import operator
import csv
from nltk.corpus import wordnet
from itertools import chain
import time,datetime,os
import collections
import traceback
import string



tokenizer = RegexpTokenizer(r'\w+')
def extract_words_in_window(tokens, named_entity, window_size_N,token_idxs):
    ne_words = named_entity.split()
    token_count = len(tokens)
    ne_word_count = len(ne_words)
    documents = []
    tokens_idx_to_remove = []
    wordvec=[]
    for i in range(token_count-ne_word_count):
        match=True
        for j in range(ne_word_count):
            if(tokens[i+j]!=ne_words[j]):
                match=False
                continue
        if(match and len([i+j for j in range(ne_word_count) if i+j not in token_idxs]) > 0):
            windows = get_window_idxs(token_count, window_size_N + 20, i, ne_word_count)
            # document = []
            # print(windows,i,token_count)
            wv=[]
            for (start,end) in windows:
                #document.extend(tokens[start:end])
                wv.append(tokens[start:end])
            # print(wv)
            window_tokens = get_word_window(window_size_N,wv[0],wv[1])
            document=[]
            # print(window_tokens)
            #print(window_tokens)
            document.extend(window_tokens[0])
            document.extend(window_tokens[1])
            documents.append(document)
            wordvec.append(window_tokens)
            for j in range(ne_word_count):
                tokens_idx_to_remove.append(i+j)
                token_idxs.add(i+j)
    output = collections.namedtuple('output', ['documents', 'tokens_idx_to_remove', 'wordvec', 'token_idxs'])
    return output(documents,tokens_idx_to_remove,wordvec,token_idxs)

def get_window_idxs(token_count, window_size_N, ne_pos, ne_word_count):
    if (2 * window_size_N) > (token_count - ne_word_count):
        return [(0, token_count), (0, 0)]
    if ne_pos - window_size_N < 0:
        return [(0, ne_pos), (ne_pos + 2, ne_pos + ne_word_count + window_size_N + (window_size_N - ne_pos))]
    if ne_pos + ne_word_count + window_size_N > token_count:
        return [(ne_pos - window_size_N + (token_count - ne_pos - ne_word_count - window_size_N), ne_pos), (ne_pos + ne_word_count, token_count)]
    return [(ne_pos - window_size_N, ne_pos), (ne_pos + ne_word_count, ne_pos + ne_word_count + window_size_N)]

def get_word_window(window_size,ltokens,rtokens):
    #print(window_size,ltokens,rtokens)
    lcount = 0
    ltokens_final = []
    l_end_idx = len(ltokens) - 1
    while lcount < window_size and l_end_idx >= 0:
        token = ltokens[l_end_idx]
        #print(token,l_end_idx)
        l_end_idx = l_end_idx -1
        if(len(token) > 1 and token not in en_stop):#'''and token in dictionary_words_set'''):
            ltokens_final.append(token)
            lcount = lcount + 1
    rcount = 0
    rtokens_final = []
    r_end_idx = 0
    while rcount < window_size and r_end_idx < len(rtokens):
        token = rtokens[r_end_idx]
        #print(token,r_end_idx)
        r_end_idx = r_end_idx + 1
        if(len(token) > 1 and token not in en_stop):# '''and token in dictionary_words_set'''):
            rtokens_final.append(token)
            rcount = rcount + 1
    if(l_end_idx < 0):
        diff = window_size - len(ltokens_final)
        while rcount < window_size + diff and r_end_idx < len(rtokens):
            token = rtokens[r_end_idx]
            #print(token,r_end_idx)
            r_end_idx = r_end_idx + 1
            if(len(token) > 1 and token not in en_stop):# '''and token in dictionary_words_set'''):
                rtokens_final.append(token)
                rcount = rcount + 1
    if(r_end_idx >= len(rtokens)):
        diff = window_size - len(rtokens_final)
        while lcount < window_size + diff and l_end_idx >= 0:
            token = ltokens[l_end_idx]
            #print(token,l_end_idx)
            l_end_idx = l_end_idx -1
            if(len(token) > 1 and token not in en_stop):# '''and token in dictionary_words_set'''):
                ltokens_final.append(token)
                lcount = lcount + 1
    ltokens_final.reverse()
    return [ltokens_final,rtokens_final]
    

# ner_occurance_count_file = open("/Users/kaushik/Desktop/MS CS/Research/Jay/Names.txt")
# ner_occurance_count = {entity.replace("+"," "):int(count) for [entity,count] in [s.split(" ") for s in ner_occurance_count_file.read().split("\n")[:-1] if not ''] }

#Change the following
directory="/Users/jayashree/Desktop/new/"
f=open("/Users/jayashree/Downloads/final_dict_condensed.json")
stop_words_file=open("/Users/jayashree/Downloads//mallet-stopwords-en.txt")
print("here")

en_stop=set(stop_words_file.read().split())
ner_location_json = json.loads(f.read())
named_entitys = ner_location_json.keys()


print("done")





name_disambi = open(directory+'NameDisambiHashWindow.txt', 'w+')
inputForNameWindow = open(directory+'inputForNameWindowHashMap.txt', 'w+')
wordVec = open(directory+'WordToVecInput.txt', 'w+')
dictionary_words_set = set(chain(*[syn.lemma_names() for syn in wordnet.all_synsets()]))
missed_files=[]
# Debugging code
# bad_ne = set(['sabrina-tagudar', 'madlon-laster', 'curtis-foster', 'pinoak-lane', 'lilian-rea', 'newton-plaisance', 'cathy-marshall', 'gary-james-smith', 'victor-cholewicki', 'harlan-ullman', 'ahmed-lemghari', 'catherine-whiting', 'helen-hayes', 'helen-hayes', 'helen-hayes', 'foss-donovan', 'david-morlier', 'joseph-mccarthy', 'catherine-chiccone', 'gordon-hagen', 'margaret-adams-parker', 'joshua-lee', 'david-blomquist', 'conor-mcgregor', 'praveen-reddy-mukkamala', 'molly-scherf', 'sue-mosher', 'colleen-ligibel', 'bruce-krebs', 'oliver-kendall', 'cheryl-mckeon', 'michael-boito', 'bill-fallon', 'eric-sahm', 'yvette-alexander-en-el-ward', 'russell-thompson-iii', 'edward-john-fallivene-iii', 'michael-tidwell', 'voeun-ngoun', 'rudaba-zehra-nasir', 'peter-theil', 'nicki-dlugash', 'paul-biedlingmaier', 'brendan-martin', 'beverley-ann-reynolds', 'winston-churchill', 'juliann-amy-coles', 'christina-friske', 'leo-bosner', 'helena-klassen', 'emanuel-joel-cohan', 'terence-kuch', 'camilla-schwarz', 'terry-parmelee', 'heidi-schramm', 'skyhill-lane', 'ryang-hui-kim', 'annette-leslie-williams', 'david-griggs', 'david-griggs', 'miroslav-styblo', 'jonas-hurtarte-rodriguez', 'mina-kamil', 'ekta-dhaliwal', 'katlo-manthe', 'antoine-dieulesaint', 'mandira-chatterji', 'james-fulton', 'mike-selckmann', 'andrew-nichols', 'evelyn-jimenez', 'jane-jaffe', 'gail-anne-morin', 'paola-navone', 'karen-galeano', 'edward-mccarey-mcdonnell', 'quartz-lane', 'megan-petratis', 'dan-bolling', 'steve-nearman', 'sherri-lauritzen', 'amanda-simmons', 'benjamin-woody', 'wayne-carpenter', 'luis-enrique-valenzuela-gil', 'mary-post', 'james-brennan', 'emily-wick', 'wheaton-michael-hodgson', 'darrel-salisbury', 'cirilo-torres', 'herminio-garcia', 'declan-leonard', 'warbler-lane', 'rick-pullen', 'james-fey', 'ona-bunce', 'jia-lu', 'deborah-ottinger', 'robin-broadfield', 'margaret-hadley', 'priscilla-kirby', 'james-patrick-harty', 'kenneth-suskin', 'saima-khan', 'kathleen-baldwin', 'stanley-cohen', 'steve-tracton', 'james-little', 'glenn-kirksey', 'jessica-nicole-abell', 'james-mallios', 'kevin-hluch', 'russell-davis', 'casey-valerie-tanoy-linsey', 'caitlin-keosha-dawkins', 'john-fay', 'barry-levine', 'robert-aubry-davis', 'carl-briggs', 'carl-briggs', 'michael-scott', 'michael-scott', 'adele-gravitz', 'laidler-campbell', 'rick-eisler', 'katie-zezima', 'katie-zezima', 'derek-stewart', 'daina-lieberman', 'thao-nguyen', 'shahina-haque', 'rick-flowe', 'rick-flowe', 'rebecca-walter', 'teri-kaufman-leonovich', 'valerie-cavalheri', 'rachel-rose', 'philip-fernbach', 'mike-mccrea', 'temidayo-adebanjo-obayomi', 'tom-mannos', 'luca-gori', 'laurent-loic-yves-bossavie', 'mike-mcgovern', 'mike-mcgovern', 'mike-mcgovern', 'rebecca-horahan', 'david-randal-allen', 'allan-wendt', 'praveen-kumar-reddy-padires', 'kwang-gil-yim', 'anne-arundel', 'anne-arundel', 'anne-arundel', 'anne-arundel', 'anne-arundel', 'anne-arundel', 'anne-arundel', 'anne-arundel', 'anne-arundel', 'craig-hoogstra', 'sabrina-gallagher', 'richard-thrift', 'catherine-benedetto', 'frank-walter', 'melek-gul', 'gary-parker', 'lenny-rudow', 'sullivan-lane', 'harry-meem', 'meg-spencer-dixon', 'lori-stern', 'ed-walz', 'thi-bich-ha-tran', 'thomas-jackson', 'abhay-kumar-srivastava', 'elsy-nohemy-batres-pleitez', 'dasari-rao', 'amber-devonte-burton', 'sivam-ramalingam', 'emmaline-elisabeth-silverman', 'chip-watkins', 'jack-mckay', 'jacqueline-wilson', 'joshua-alan', 'brad-botwin', 'natalia-franco-millan', 'madison-lane', 'martha-piesto', 'emmanuel-macron', 'emmanuel-macron', 'timothy-snider', 'cliff-harrison', 'joseph-paul-dewald', 'columbia-pike', 'ralph-buck', 'sherri-deck', 'cedar-ramos', 'collin-parsons', 'maggie-vinskey', 'terry-carter', 'robin-payes', 'sal-culosi', 'frank-ierardi', 'virginia-billhimer', 'van-nguyen', 'michael-shaw', 'steve-crittenden', 'roberta-huber', 'daniela-mcfarren', 'stephen-knieriemen', 'jim-speicher', 'michael-patrick-foss', 'scott-williams', 'somboon-dittaboot', 'sally-stoecker', 'ryan-kielczewski', 'jean-francois', 'eric-shulman', 'mary-nolan-hedrick', 'doris-rausch', 'doris-rausch', 'kara-kamuda-sartain', 'karen-rudolph', 'ed-kelly', 'christopher-forney', 'rhonda-chocha', 'benita-veskimets', 'wilton-corkern', 'maria-erlinda-giron', 'kristen-hamilton', 'aprill-rothman', 'casey-dowell', 'kevin-james', 'eric-hall', 'alison-fields', 'john-kasich', 'bill-montross', 'erik-sundquist', 'leslie-ekstrom', 'beka-chanturia', 'sol-schindler', 'philip-fernbach-riverhead', 'andrea-bouchard', 'sam-soliman', 'kristin-joanne-camacho', 'julie-dunlap', 'kyle-burney', 'travis-sherman-wells', 'jere-rutt', 'michael-daly', 'kil-soon-shin', 'istvan-dobozi', 'istvan-dobozi', 'tina-schellman', 'vincent-othieno', 'jonathan-bledsoe', 'michael-rosenthal', 'edwin-cubbison', 'audrey-gaquin', 'katherine-ziskind', 'nicole-fourie', 'phil-griffin', 'tany-martinez', 'matthew-scott', 'chris-rutledge', 'tom-spiggle', 'george-brockway', 'croft-way', 'nancy-katz-triplett', 'ross-pickford', 'larry-gondelman', 'paula-moore', 'jim-beller', 'fitsum-dori', 'beatriz-prieto-oramas', 'puja-kumar', 'karen-meyers', 'reina-delapaz-ramirez', 'nancy-luria', 'jose-samuel', 'susan-dougherty', 'sara-downes', 'roy-deppa', 'david-kelly-horvath', 'fred-barbash', 'ed-mouton', 'briggs-way', 'mary-petersen', 'agheddou-youssef', 'ana-maria', 'tyler-alexander', 'sara-white-ruschaupt', 'praveen-madhiraju', 'elizabeth-kingery', 'timothy-cooper', 'camille-newsome', 'beatrice-pougoum', 'susan-hale', 'victor-otero', 'elaine-montgomery', 'frank-brodersen', 'mary-amanda-keifer', 'eldridge-gayer', 'dee-schmitt', 'jim-morris', 'foxfield-lane', 'jeff-green', 'rose-kelleher', 'yuiza-arce', 'robert-hunter', 'kay-belisle', 'jack-hadley', 'heather-jones', 'cindy-mcmanes', 'carol-lee-baldwin', 'zachary-adamd-reph', 'sara-tilson', 'gregory-grimes', 'louise-gallagher', 'anna-hegeler', 'allison-roush', 'van-buren', 'al-miller', 'eloise-hudgins', 'robert-stewart', 'efren-castro-escobar', 'daniel-thomas', 'eileen-tydlacka', 'centerton-lane', 'sean-kelly', 'larry-butler', 'yolandah-knopp', 'ken-wenzel', 'danielle-polizzi-fox', 'faith-berens', 'auston-matthews', 'annette-puckett-wickes', 'christina-szerlip', 'nick-melczarek', 'anne-holloway', 'anne-holloway', 'larry-kahn', 'kim-soffen', 'bruce-carnes', 'daniel-dzurek', 'dawn-saddler', 'manuel-aguirre', 'bill-butler', 'avram-israel-reisner', 'avram-israel-reisner', 'elizabeth-mcwatty-heim', 'emily-pomeranz', 'warren-milberg', 'nick-pappas', 'gerald-chandler', 'gladys-tordil', 'richard-hayman', 'michael-moore', 'bernard-ohanian', 'brian-courchaine', 'dennis-askwith', 'carl-proper', 'david-spindler', 'anthony-davis', 'mohmoud-hassanen', 'karen-brown', 'lina-sandouk', 'rosemary-ciccarelli', 'joan-harrison', 'jacques-kapuscinski', 'adam-fairclough', 'toni-paul', 'dawn-michelle-malinosky', 'larry-kugler', 'el-kamla-izdarh', 'greg-raleigh', 'greg-raleigh', 'sayed-salahuddin', 'sayed-salahuddin', 'joel-whitaker', 'heidi-lee-stone', 'marjorie-owens', 'ed-rogers', 'shannon-kathleen-ament', 'sonia-rothschild', 'jules-verne', 'jose-ricardo-garcia', 'pete-rose', 'steve-postal', 'lee-hurwitz', 'lee-hurwitz', 'chuck-norris', 'dan-edwards', 'sami-khedira', 'rebecca-hertzman', 'katherine-rose-anderson', 'pamela-parsons', 'brent-budowsky', 'rajesh-david-paul', 'ed-houry', 'alfred-duncker', 'van-thi-kim-nguyen', 'ghazaloh-aram', 'jonas-moore', 'daniella-woo-kang', 'ed-gillespie', 'del-pino', 'jane-hannaway', 'tingting-wang', 'david-brunori', 'sara-khin-ku', 'elizabeth-quill', 'henry-jairzinho-ura-quispe', 'jason-chaffetz', 'glen-allen', 'jean-biniek', 'mark-rosenman', 'padmanabha-reddy', 'melane-kinney-hoffmann', 'ray-phillip', 'richard-alexander', 'warren-emerson', 'mikayla-kurland', 'barack-obama', 'barack-obama', 'sweetleaf-lane', 'kyle-bagin', 'sharon-hurley', 'hamish-waugh', 'eric-hollins', 'bob-hugman', 'bob-hugman', 'chrissa-rogers', 'terri-lynn-poole', 'wickham-way', 'rasmey-ky', 'valentina-echeverry', 'paule-zapatka', 'roger-kurrus', 'scott-henrichsen', 'richard-joffe', 'steve-jenning', 'steve-jenning', 'joseph-giorno', 'nathan-wittstruck', 'eryn-sepp', 'bill-conrad', 'maria-veronica-alvarez', 'allen-farrish', 'peggy-dennis', 'jay-brock', 'dora-henriquez-de-vigil', 'philip-justus', 'ann-simon', 'nancy-berry', 'brian-michael', 'mariam-akhtar-quettawala', 'keith-ord', 'alice-mascette', 'david-pawel', 'saddam-hussein', 'brian-petty', 'brian-petty', 'joshua-leard', 'michelle-obama', 'gerald-kelly', 'mike-bohn', 'luis-quintero', 'daniel-island', 'bernice-stein-fabbrini', 'julie-phillips', 'marion-goldin', 'steve-hassan', 'michael-wolfson', 'nessler-hayden', 'elizabeth-chawla', 'carol-calvert', 'jennifer-girard-smith', 'dorothy-heather-fox', 'lyle-sinrod-walter', 'constance-schmidt', 'waddah-albayaa', 'hyo-sook-kim', 'lindsey-hoggle', 'lindsey-hoggle', 'liane-rozzell', 'su-ah-joo', 'matt-murray', 'michael-adelman', 'tom-martella', 'tom-martella', 'zekan-lane', 'lisa-paschal-snyder', 'olga-yanet-gomez', 'ritha-khemani', 'putnam-lane', 'gordon-eliot-white', 'lynn-margaret-filpi', 'cory-emery', 'nancy-linares', 'robert-zimmerman', 'james-todd', 'robert-honig', 'reginald-schoonover', 'joan-reinthaler', 'maria-isabel-dungas-simmerman', 'venus-rochelle-laberge', 'naheed-arshad', 'sarah-shoenfeld', 'michael-mendelson', 'charles-krauthammer', 'charles-krauthammer', 'ben-guthrie', 'susan-leverone', 'sandra-yesenia-quiteno-de-contreras', 'karen-ahlquist', 'cyril-ignatius', 'daniel-lewis', 'elizabeth-dinan-winnike', 'rob-shutler', 'david-sproul', 'maurice-jackson', 'ken-goldstein', 'valerie-collentro', 'dana-vollmer', 'julie-hirka', 'julian-klazkin', 'naomi-miller', 'suki-chon-nam', 'kevin-green', 'jim-rush', 'ken-pilkenton', 'bernard-wilder', 'robert-mccarthy', 'walter-dellinger', 'william-bethel', 'michael-hurd', 'bruce-johnson', 'barry-mendelsohn', 'barry-mendelsohn', 'nikoo-faghih', 'bill-miller', 'matthew-barton', 'benjamin-letzler', 'preston-wilson', 'kristin-liautrakul', 'joel-storms', 'andre-sauvageot', 'jeanine-hull', 'ivan-esteva', 'james-joseph', 'ellen-locke', 'lisa-sraders', 'greg-grapsas', 'dawson-nash', 'harry-hamilton', 'elizabeth-mcgonagle', 'rachel-reilly-carroll', 'marilyn-urwitz', 'laurence-joseph-marhoefer', 'ana-sicer', 'arlene-schorr', 'simon-ernst', 'anne-atwood', 'andrew-miller', 'janet-windsor', 'greg-wahl', 'shirisha-reddy-basireddy', 'gary-weitzner', 'michael-vann', 'jesus-christ', 'rebecca-rose-clark', 'jeffrey-sartin', 'james-allen', 'joan-makurat', 'carolyn-lieberg', 'marcia-augstine', 'chrissy-teigen', 'elnur-baimov', 'guy-lawson', 'jim-giza', 'john-fuller', 'susan-schulman', 'ruth-eglash', 'elizabeth-smith-brownstein', 'jen-crittenden', 'yaping-wang', 'tara-varghese', 'claudio-alatorre-frenk', 'claudio-alatorre-frenk', 'elizabeth-rouse-defrancesco', 'scott-parker', 'stephen-surrells', 'maria-brooks', 'paula-fedrici', 'jeffrey-stein', 'linda-yahn', 'jennifer-oakley', 'sayera-parveen', 'barry-blyveis', 'jim-kellett', 'jim-kellett', 'eduardo-alache', 'dorothy-lin', 'peter-dunner', 'kyle-jones', 'louis-meyers', 'louis-meyers', 'betty-nyan-amekudzi', 'samuel-lee', 'delia-marie-gordon', 'rita-zeidner', 'panos-kakaviatos', 'may-chea', 'david-wheeler', 'crabtree-way', 'michael-puglia', 'david-repass', 'dale-davidson', 'harold-paul-luks', 'hilton-head', 'hilton-head', 'alice-nicolson', 'danny-mixon', 'john-sitilides', 'saffron-lane', 'deanna-kim', 'will-driscoll', 'lawrence-ink', 'mirena-hine', 'jeffrey-cooper', 'walter-ford', 'albert-nekimken', 'vintage-lane', 'theresa-gleim', 'herb-butler', 'william-steigelmann', 'paul-trampert', 'theresa-natishan', 'jennifer-grisham', 'linda-silversmith', 'arnold-lane', 'bill-cowdrey', 'leslie-kingsley', 'gene-fellner', 'salad-olivier', 'art-vidrine', 'lourdes-adonia-acosta', 'anthony-papa', 'ashleigh-bicevskis', 'susan-scott-fodness', 'steve-norton', 'phoebe-bacon', 'marsha-schmidt', 'marsha-schmidt', 'susan-chen', 'van-hollen', 'van-hollen', 'bill-clinton', 'karin-pedrick', 'sara-mcalpine', 'bill-noack', 'herley-david-giles', 'sa-kvong-kim', 'jose-altuve', 'laura-bernstein', 'nathaniel-lyn-gailey-schiltz', 'iffat-sattar-chaudhry', 'ayman-hakki', 'ernie-levy', 'ernie-levy', 'richard-sheres', 'jonathan-strong', 'janet-devine-smith', 'marilyn-sumpter', 'beryl-lieff-benderly', 'elise-manuel', 'meredith-anne-cernak', 'christopher-garvey', 'fred-kranz', 'william-george', 'lester-shapiro', 'sue-yun-kim', 'vance-garnett', 'smoketree-lane', 'david-luban', 'jeanette-krote', 'cam-newton', 'cam-newton', 'nancy-pace', 'vell-rives', 'bill-holdsworth', 'king-george', 'chris-giannella', 'krissah-thompson', 'martin-luther-king', 'angela-childs', 'andrea-saba-torrejon', 'shashank-singh', 'john-loftus-ball', 'grey-dal-adkins', 'grant-brownrigg', 'robert-schlubach', 'rick-friedman', 'william-hicks', 'bhimsen-kathayat', 'donald-niday', 'highcourt-lane', 'tricia-spencer', 'jesse-mcaninch', 'edward-basile', 'roger-hartman', 'roger-hartman', 'arthur-schwartz', 'karen-buglass', 'andrea-figueroa', 'julia-larson-wurglitz', 'al-friebe', 'chris-hester', 'margaret-treacy', 'randle-el', 'laszlo-kutor', 'jan-wessling', 'roger-cohen', 'paula-miller', 'hyo-joo-choi', 'valerie-spiegler', 'david-schoen', 'cherry-lane', 'sara-rix', 'sheila-klotz', 'brenda-koshie-mills-layne', 'joan-chamberlain', 'lambert-lam', 'clara-shannon', 'howard-walderman', 'howard-walderman', 'joshua-raymond-marshall', 'joy-marie-watkins', 'sandra-sloan', 'patrick-judd-murray', 'george-chuzi', 'samantha-walker', 'philip-frankenfeld', 'patrick-john', 'jose-carlos-linares', 'samuel-bishop', 'maple-grove-lane', 'paul-kerrigan', 'donna-widawski', 'christian-gangitano', 'danielle-shover', 'mary-kruger', 'brian-carlson', 'nathan-lane', 'reg-mitchell', 'david-biderman', 'meghan-parker', 'pedro-maya-clotet', 'sabina-sadirkhanova', 'pauline-fox-sullivan', 'demetria-scott', 'doug-griffith', 'david-wolinsky', 'quail-run-lane', 'mehryar-minaie', 'samuel-isaac-stewart', 'james-bisher', 'chad-harvey', 'henry-scott', 'hans-bader', 'john-boese', 'clemencia-amir', 'margaret-davenport', 'joseph-herb', 'david-simpson', 'kevin-rodden', 'wun-sook-kang', 'beverly-christiana', 'charnice-milton', 'zack-shaeffer', 'zack-shaeffer', 'ana-maria-marin', 'carmen-villanueva', 'sylvia-coulter', 'jasvinder-kaur', 'stephanie-lieberman', 'linda-burchfiel', 'peter-wolfe', 'david-culp', 'sik-lee', 'basil-fomanka', 'brian-heater', 'nelson-goodman', 'kevin-brady', 'pamela-kincheloe', 'michael-ohlen-wells', 'helen-dalton', 'rose-marie-woodsmall', 'lisa-roney', 'robert-wieder', 'susan-creed-percy', 'paul-hurley', 'matthew-paul-brown', 'matthew-paul-brown', 'robert-berman', 'aaron-kane', 'marvin-chen', 'tsun-chien-chen', 'soojin-kim-lee', 'eric-echols', 'maliha-jilani', 'natalie-buda-smith', 'teoshi-lavonda-edwards', 'elizabeth-buckingham', 'cedric-alexander', 'jamie-insley', 'joan-hartman-moore', 'gail-ayres-davies', 'lewis-gollub', 'virginia-odierno', 'dana-johnson', 'stephen-robin', 'talivaldis-ivars-smits', 'atwood-lane', 'thomas-cieslak', 'jim-lamont', 'marvin-elster', 'pegah-ebrahimi-eftekhari', 'kathleen-parker', 'meg-copernoll', 'karl-kettler', 'mike-salmon', 'jeff-hazle', 'jenay-morrisey', 'jill-holtzman-vogel', 'florin-way', 'kyungok-kim', 'carol-leos', 'brent-berger', 'michael-conboy', 'vilai-ditaboot', 'ed-merlis', 'jane-krakowski', 'janalee-redmond', 'edwin-fabian-carrillo-lopez', 'sarah-janaro', 'barbara-kaufmann', 'joseph-hawkins', 'james-michael-novak', 'heidi-martin', 'matthew-jemal', 'malcolm-lloyd', 'terry-ann-larus', 'bruce-dimon', 'viadislav-belyaev', 'kevin-burke', 'bill-hegedusich', 'john-mcgovern', 'ed-rader', 'electra-beahler', 'sean-conboy', 'donald-trumps', 'julie-victoria-kapp', 'phuong-thao-thi-nguyen', 'mary-ann-dimola', 'marie-kassir-payne', 'lisa-cline', 'steve-amoia', 'miranda-lambert', 'pat-hayden', 'arina-van-breda', 'arina-van-breda', 'suey-newcomb', 'david-sarokin', 'hillary-clinton', 'hillary-clinton', 'charlene-drew-jarvis', 'paul-foldi', 'carolyn-marie-richon', 'chiswick-lane', 'sean-hogan', 'christopher-smith', 'elizabeth-mullen', 'millbrook-terr', 'sean-aaron-shortridge', 'daniella-kalinowski', 'mary-combs', 'lynn-frankenfield', 'shabina-rahman', 'john-hattin', 'mariam-osmun', 'mary-ann-ryan', 'wilhelm-stenhammar', 'megan-keller', 'fred-borch', 'richard-hale', 'patrick-heron', 'sigrid-benson', 'singh-madhok', 'joseph-debor', 'ben-rhodes', 'donna-weiss', 'knollside-lane', 'lance-rees', 'pavan-kumar-punati', 'soumiya-benseghir', 'ron-cohen', 'yucheng-chu', 'christopher-ian-morris', 'clayton-mark', 'meghan-gearon-herzing', 'brian-kelley', 'andrew-boyd', 'nancy-stanley', 'nancy-stanley', 'anthony-hathaway', 'leilah-reese', 'doug-peterson', 'melanie-smit', 'tom-barber', 'william-thomson', 'albagir-ibrahim-adam', 'john-lucas', 'gabriel-salmeron', 'muhammad-shakeel-ahmad', 'laurie-pearl-friedman', 'christopher-mccarthy', 'ken-mclean', 'lee-carpenter', 'bonifacio-gonzalez-pelaez', 'aaron-kahana-casey', 'eva-nahid', 'julie-cole-lux', 'kimberly-callahan', 'gnansundari-janakiraman', 'lucinda-low-swartz', 'eric-greene', 'heather-yuzvenko', 'zeynep-karatas', 'sylvia-diane-sumrall', 'george-taft', 'darren-mckinney', 'harold-datz', 'stephanie-leggette', 'edwardene-lane', 'jack-lichtenstein', 'thomas-oudone-suryadeth', 'daniel-stafford', 'ido-rabinovitch', 'ryan-craig', 'linda-linton', 'john-michael-dugan', 'adam-anthony', 'ryan-brown', 'cristina-delaney', 'desiree-lee-vyas', 'melissa-mclawhorn', 'julianne-kerr', 'rufus-king', 'nicole-bernadette-racadag', 'leroy-laroche', 'liza-kirchoffer', 'mary-lee-pence', 'barbara-osgood', 'joan-mcintyre', 'tom-polgar', 'matthew-murguia', 'giselle-soriano', 'phoebe-cooke', 'kelly-mack', 'nicolas-deslauriers', 'barbara-sarshik', 'cynthia-greer-moore', 'edidah-kyamazima', 'van-dyke-baer', 'kaiser-farooque', 'mary-nimmerrichter', 'christopher-steven', 'yulan-long', 'chris-homan', 'andis-maris', 'carla-irma-valle', 'suzanne-johnson', 'suzanne-johnson', 'chris-walsh', 'chris-walsh', 'marianne-warner', 'dean-pineles', 'robert-steinglass', 'daniel-moss', 'barbara-dinsmore', 'robert-leary', 'megan-stafford-dolezel', 'cora-diamond', 'robert-posner', 'robert-posner', 'garrett-heath', 'sean-connolly', 'donna-nelson', 'martine-gordon', 'linda-rosendorf', 'lee-ya-mei-yeh', 'alfred-munzer', 'james-frank-klemic', 'robert-ray', 'n-sang', 'bonnie-boyle-cote', 'bonnie-boyle-cote', 'tricia-campo', 'terrell-roberts', 'davis-fairfax-lane', 'angela-marie-devierno', 'katy-cannady', 'kevin-healy', 'mike-weinberger', 'brian-gurr', 'nana-akomeah', 'kristin-ct', 'john-peters-irelan', 'carla-mae-reser', 'joe-stowers', 'ainsley-michele-decker', 'christopher-groskopf', 'john-mathwin', 'john-mathwin', 'larry-regan', 'lori-laws', 'tiffany-allen', 'phyliss-terrell', 'evan-scott-thomas', 'evan-scott-thomas', 'mike-wicklein', 'shola-oyewole', 'mary-boyd-click', 'sarah-botsai', 'richard-ball', 'charles-town', 'francis-wray', 'catherine-cory-verdier', 'denis-cotter', 'ahmed-ibrahim', 'chuck-wilson', 'john-warshawsky', 'stan-schwartz', 'greg-versen', 'greg-versen', 'abaynesh-negash-desalegn', 'carrie-johnson', 'ron-battocchi', 'jean-lin', 'oscar-ramos', 'arnold-palmer', 'tewodros-abebe', 'ross-shearer', 'ed-heaton', 'ronald-lipford', 'robert-goren', 'jeanne-kadet', 'karin-susana-troncoso-torrez', 'charles-werchado', 'craig-finn', 'joseph-lowry', 'andrea-lee-rozner', 'jeff-fischer', 'will-jawando', 'joe-palka', 'christine-hunt-szympruch', 'yang-yu', 'fengju-zhang', 'dorothy-baldwin-wicker', 'megan-blandchard', 'radhika-sambaru', 'christine-schmidt-bray', 'rob-portman', 'ivan-halley', 'tewodros-getahun', 'linda-asaf', 'sarah-soojung-park', 'george-diffenbaucher', 'john-mohr', 'johanna-warren', 'bob-understein', 'cara-daggett', 'ann-keegan', 'dwayne-wade', 'katherine-nicole-pirolt', 'george-bogart', 'dave-lombardo', 'carol-sottili', 'kirby-kingsley', 'frank-kohn', 'louis-golino', 'ronald-reagan', 'chris-rigaux', 'steven-allen-upp', 'hughes-allento-lee-jung-hong', 'john-roberts', 'tammy-tyeryar', 'victoria-anne-curry', 'corrita-myers', 'dilip-ramchandani', 'ann-marie-peters', 'sonaal-luthra', 'jianghong-wang', 'samatha-marie-marshall', 'prentiss-de-jesus', 'virginia-fernbach', 'patricia-griffin', 'jean-garneau', 'kevin-tyrone-waller', 'sally-alexander', 'terry-dyroff', 'peter-kimm', 'alan-peterson', 'nancy-reyes', 'nueva-york', 'gina-thacker', 'rosalind-feldman', 'laura-simon', 'patricia-weakley', 'cobbler-terr', 'christine-laurich', 'margaret-crenshaw', 'joe-clement', 'charles-tievsky', 'chi-thikim-truong', 'gene-poteat', 'alexandra-beshara', 'gehendra-raj', 'callie-tweedale', 'kirk-barrow', 'pramod-khadka', 'ron-sheppe', 'jennifer-mclaughlin', 'rob-abbot', 'rob-abbot', 'sergio-frias', 'chi-wang', 'madeleine-mccollough-lottenbach', 'trump-jill-goldenziel', 'van-david-hare', 'victoria-may', 'melissa-etehad', 'barbara-marie-gilbert', 'chelsea-carlson', 'katherine-marshall', 'michael-parrish', 'robert-land', 'jacquenitte-prillman', 'kelly-smith', 'marshall-cohen', 'thomas-hicks', 'alexey-lebedinksiy', 'gary-clemetson', 'gary-clemetson', 'thomas-bleha', 'birdcherry-lane', 'hyun-lee', 'keith-henderson', 'carol-morgan', 'brianna-russell', 'jerome-reiff', 'gloria-siblo', 'anu-grabbi', 'sam-scheiner', 'ikendra-lee', 'cathy-lewis', 'elizabeth-shannon-lovette', 'diane-joan-klatzman', 'xavier-brown', 'douglas-george', 'kumar-barve', 'bryon-stiftar', 'dorothy-michele-glock-trust', 'kimberly-whittet', 'graciela-rae-williamson', 'adam-weinstein', 'therese-martin', 'richard-swift', 'edward-tabor', 'edward-tabor', 'robert-chiles', 'robert-chiles', 'christopher-ambrose', 'scott-donald-mahlik', 'melanie-susan-taylor', 'dick-saslaw', 'linda-keene-solomon', 'jesse-tyler-ferguson', 'susan-kneller', 'richard-johnson', 'inga-fowler-spurlock', 'robert-phillips', 'barbara-miller', 'richenda-van-leeuwen', 'william-ramallo', 'robert-vinson-brannum', 'carol-anderson', 'bobby-baum', 'amy-golen', 'anusha-vemula', 'theresa-sullivan-twiford', 'alex-machina', 'john-supp', 'bob-moore', 'maria-del-rosario-bonilla', 'jennifer-halvaksz', 'johnny-perez', 'edward-mcmanus', 'edward-mcmanus', 'edward-mcmanus', 'edward-mcmanus', 'dee-foscherari', 'amanda-manes', 'bret-baier', 'kitska-rene-garrison', 'chris-payne', 'stacey-ecoffey', 'derek-pelham', 'angel-morris', 'sheri-langford', 'melissa-anderson', 'rhonda-pl', 'barbara-ramsey', 'gwen-geohaghan', 'maxwell-cohen', 'benjamin-clementine', 'annaliese-wiederspahn', 'sidney-johnson', 'arlyn-zuniga-gomez', 'arthur-cotton-moore', 'candace-kent', 'sandra-barros-hylton', 'ted-benjamin', 'bruce-fein', 'bruce-fein', 'barry-altman', 'pabitra-adhikari', 'nickolaus-mack', 'jamie-raskin', 'william-lukens', 'mohammad-akbar', 'grayvine-lane', 'heidi-gibson', 'bill-harper', 'douglas-johnson', 'ana-zarkua', 'robert-hennemeyer', 'thomas-murphy', 'david-piachaud', 'mary-frances-fetzer', 'hae-dong-lee', 'dennis-kirk', 'jason-anthony-palumbo', 'susan-altman', 'susan-altman', 'elliot-wilner', 'elliot-wilner', 'david-miller', 'randy-hardee', 'jacqueline-monica-nyren', 'dan-peacock', 'joe-miller', 'joseph-clair', 'matthew-decamp', 'joshua-smeraldi', 'herbert-chubin', 'john-diskin', 'mari-angelli-escobales-robles', 'walter-hadlock', 'emily-breeding', 'amalie-laurich', 'wallace-babington', 'wallace-babington', 'michele-robin-anderson', 'robert-joseph-kraft', 'mary-grafton', 'freddie-mac', 'ria-manglapus', 'ria-manglapus', 'daniel-lawrence-mode', 'shalin-brine-pizzo', 'kempston-lane', 'jyothi-mula', 'steve-hyde', 'tamara-bauer', 'david-berry', 'dale-kaufman', 'michaela-west', 'claudio-hayes', 'ian-clements', 'kathleen-pedersen', 'terry-murray', 'peter-johnson-butkus', 'marie-miller', 'fairwood-lane', 'raihona-atakhodjaeva', 'herb-miller', 'joanne-rocky-delaplaine', 'kathleen-kerry-davenport', 'matthew-freedman', 'robin-teresa-hart', 'geoff-drucker', 'john-oliver', 'stephen-barlas', 'mary-flannery', 'zhen-zhong-wu', 'henry-antonio', 'david-keir', 'marcia-ruth', 'sarah-larimer', 'lacey-wootton', 'jennifer-riess', 'michael-joseph', 'anthony-john-ceraolo', 'steven-shore', 'louanne-wheeler', 'sam-golan', 'charles-anthony-crossed', 'kyeongsoo-seol', 'brenda-bidinger', 'dante-novicio', 'marsha-dubrow', 'marsha-dubrow', 'atom-willard', 'karen-vanessa-dixon', 'joan-grey', 'virginia-alcocer', 'alistair-millar', 'raymond-orkwis', 'joshua-tree-lane', 'al-dicenso', 'tom-marshall', 'jane-elizabeth-prince', 'alicia-carmody', 'lawrence-schwartz', 'jeff-flake', 'christine-ledbetter-read', 'marci-el-baba', 'keith-oberg', 'michael-massey', 'kimberly-destefano-larsen', 'amy-patricia-moore', 'william-sanjour', 'william-sanjour', 'zivar-hallaji', 'gregory-orfalea', 'robert-burney', 'ann-ziegler', 'larrie-greenberg', 'ralph-nader', 'erika-rivera', 'joseph-elawabdeh', 'ben-vernia', 'jill-roessner', 'jill-roessner', 'kathy-ellis', 'kimberly-poppleton', 'mary-kay-stine', 'pamela-ward', 'john-albert-sautter', 'kevin-mcnamara', 'kevin-mcnamara', 'cecile-garrett', 'earlene-shaff', 'earlene-shaff', 'mike-pence', 'joan-mcqueeney-mitric', 'suzanne-brennan-firstenberg', 'patrick-dozier', 'scott-torres', 'alex-santos', 'william-harris-way', 'alfredo-prieto', 'david-nicholas', 'virginia-gergoff', 'christine-conn', 'santiago-testa', 'jennifer-lynn-labor', 'jamie-penny', 'jill-corrigan', 'knockeyon-lane', 'dan-reuter', 'ken-stohr', 'jane-litchtman', 'michelle-kalmanson', 'nisha-putnambekar', 'amy-granger', 'xue-zhu-zheng', 'tom-hoffman', 'scott-shultz', 'heeju-jang', 'marchant-wentworth', 'barry-kowalski', 'melissa-hecht', 'bob-lindsey', 'teresa-reilly', 'hicham-dadouche', 'nicole-mittendorff', 'marian-lapp', 'malcolm-odell', 'janet-oshinrongboye', 'carolyn-proctor-fort', 'chris-flannagan', 'horseshoe-lane', 'cathryn-zucker', 'jeb-bush', 'li-hua', 'fritz-mulhauser', 'lydia-banks', 'shu-wen-cheng', 'alan-migdall', 'alan-migdall', 'norman-leventhal', 'tom-price', 'andrew-gall', 'barbara-leaf', 'yeshialem-zerihun', 'peter-thomas', 'carla-urquhart', 'steve-livengood', 'steve-livengood', 'emily-guandique', 'frank-green', 'leona-lane', 'scott-ableman', 'john-marshall', 'matthew-tompkins', 'jud-fisher', 'jessica-lincoln', 'jennifer-fries', 'nadia-boschi', 'owings-mill', 'anita-louise-hughes', 'cathleen-cuddahee', 'cathy-alifrangis', 'nihal-horne', 'bonnie-becker', 'nathaniel-allen', 'tony-tran', 'dana-hedgpeth', 'kathleen-schirf', 'deidre-smallwood', 'mike-kendellen', 'rick-perry', 'barbara-blaylock', 'donald-mccabe', 'herman-alexander', 'cassandra-ma', 'renee-long', 'herb-savage', 'larry-tracy', 'mark-horowitz', 'mark-horowitz', 'rick-barry', 'ada-carter', 'wendelyn-duke', 'ismary-elizabet-quinteros-ramirez', 'steven-roy', 'mac-bogert', 'durga-charan-panda', 'nafees-fatima', 'shahad-nejaim', 'bahman-zahedi', 'santina-anthony', 'rose-rodriguez', 'mark-berman', 'madison-renee-ter', 'ming-jing-chen', 'doralee-simko', 'steve-rabson', 'laurence-peters', 'mickey-mouse', 'dennis-price', 'john-little', 'joe-flader', 'victoria-lebeaux', 'brock-bevan', 'chadds-landing-way', 'chia-yuan-cheng', 'thomas-stukes', 'michael-crockett', 'bob-irvin', 'carol-kyle', 'ashley-elizabeth-wampler', 'rudolph-hirsch', 'eric-sorensen', 'chris-soares', 'martin-moesel-barna', 'dana-lorayne-nance', 'beatriz-corvera', 'margaret-holly-strickland', 'jo-ann-ginsberg', 'michael-berenhaus', 'emma-kerr', 'george-hayn', 'bob-meyer', 'kristen-lyon-karpusiewicz', 'keith-sendall', 'stephanie-miller', 'hira-tanveer-hsiao', 'barbara-elisse-najar', 'dan-xi', 'jose-hernan-martinez', 'daniel-abrahamson', 'scott-baio', 'roger-ailes', 'paula-marie-krause', 'aung-naing-soe', 'mark-moessinger', 'sandra-renner', 'gregory-scott', 'ratna-saud', 'william-bateson', 'tad-daley', 'orysia-pylyshenko', 'juan-ignacio-jacome-marchan', 'juan-ignacio-jacome-marchan', 'bob-blair', 'claire-mcmanus', 'danielle-wynincx-kelly', 'vabhave-sharma', 'ting-liu', 'elizabeth-fogarty', 'abe-cunningham', 'abe-cunningham', 'rachel-sanders', 'edward-stern', 'teona-kacharava', 'abu-khatalla', 'les-brindley', 'june-schmitz', 'michael-kranish', 'sivakumar-tadikonda', 'paula-lazor', 'joyce-ann-enterline-bender', 'rosemeade-pl', 'sharon-lynn-damren', 'mary-anne-penner', 'ann-loikow', 'john-simon', 'chris-hennemeyer', 'amy-kokkinos', 'steven-sellers-lapham', 'steven-sellers-lapham', 'shannon-kathleen-tierney', 'jessica-lynn-gorey', 'antima-bhadoriya', 'kay-gartrell', 'andrew-gilbert', 'lance-graham', 'milena-amaya', 'thomas-ward', 'arnold-kahn', 'judith-oppenheimer-loth', 'rene-logan', 'yana-markey', 'susan-varghese', 'devonshire-lane', 'adriana-gonzales', 'ana-gladys-peterson', 'virginia-vincent', 'rudy-giuliani', 'howard-gofreed', 'shishir-sriramoju', 'michael-canes', 'edward-steinhouse', 'joseph-zaczyk', 'richard-danielsen', 'chick-walter', 'clare-grosgebauer', 'tyler-mitchell', 'haiyan-liu-croft', 'sharon-elaine-vansant', 'heather-foglio', 'theodore-leinwand', 'pen-suritz', 'pen-suritz', 'hank-wallace', 'brandon-tyler', 'bob-mccarthy', 'walter-reed', 'mike-blivess', 'beverly-levesque', 'stephen-rodgers', 'lanee-reaves', 'fatina-ullah', 'jim-todd', 'john-gregoire', 'joshua-mark-falbo', 'shelagh-smith', 'quyen-duong', 'kyungmi-geggus', 'priscilla-marsh', 'joelle-prosser', 'connie-deng', 'christine-matthews', 'zachary-thomas-poncheri', 'harriet-lane', 'reginald-tolbert', 'michael-mercer', 'sean-handerhan', 'val-kehl', 'daniel-frank', 'donald-poole', 'bob-heiss', 'siegman-gaynor', 'geoff-edgers-read', 'robert-lightsey', 'yukon-lane', 'edgar-antonio-zuniga', 'paul-whittemore', 'nik-philipsen', 'rhonda-shepherd', 'eric-duncan', 'ellen-bass', 'tim-foresman', 'robert-brenner', 'brandon-jerrersson', 'noreen-feddis', 'timotea-del-carmen-osorto-martinez', 'douglas-gorecki', 'john-taylor', 'lindsay-amodio', 'paul-deceglie', 'oluwatosin-oluokun', 'jeff-gorsky', 'jim-vance', 'antje-kharchi', 'manuel-lerdau', 'bob-benna', 'michael-phelps', 'jonathan-krall', 'andy-walko', 'buckner-shaw', 'cheryl-dawn-garrison', 'frederick-winter', 'jerry-laffey', 'jerry-laffey', 'jackie-kramer', 'david-lapan', 'sonia-noemi-lopez-rodriguez', 'harold-reis', 'romney-wadsworth', 'joann-easley', 'sujata-emani', 'helaine-cohen', 'jason-killian-meath', 'deb-matherly', 'nadia-hussain', 'dawn-eileen-callaway', 'daniel-golden', 'romina-espinosa', 'anne-donnelly', 'david-krane', 'eric-sapirstein', 'wolf-trap', 'thomas-salander', 'leslie-smith', 'aaron-williams', 'walter-woodson', 'terry-mcauliffe', 'frank-silnicky', 'gerald-issac-prado-perez', 'shavanna-jagrup', 'erika-flores', 'kishore-papineni', 'david-rothfeld', 'david-lytel', 'lisa-marie-batt', 'paraswati-sarkar', 'laura-smith', 'bruce-wright', 'laurie-lieberman', 'joshua-johnson', 'natalia-shamshyna', 'amy-brittain', 'amy-brittain', 'ann-wass', 'ronald-cohen', 'phil-piemonte', 'sean-roman-strockyj', 'jackie-tortora', 'betty-walter', 'betty-walter', 'kathleen-kust', 'maria-sue-landini', 'elizabeth-sienkiewicz', 'tony-magliero', 'bridgend-run', 'andres-taborga', 'daniel-zewolde', 'deborah-brody-hamilton', 'samuel-joseph', 'joshua-sinai', 'andrea-martens', 'dean-havalieratos', 'rajini-mandapalli-kennedy', 'randy-edsall', 'benjamen-lambert', 'david-jonas-bardin', 'david-jonas-bardin', 'david-jonas-bardin', 'rosario-roose', 'jian-zhou', 'john-swenson', 'matt-shannon', 'thomas-lee-way', 'deborah-schumann', 'mark-saddler', 'jim-frazer', 'david-routt', 'charles-anthony-wilson', 'timothy-gibson', 'samuel-ellman-ennis', 'sam-ricks', 'gary-newman', 'jessica-anne-andrade', 'william-nack', 'natalie-cassandra-leyton', 'margie-tompros', 'esther-finder', 'dana-alice-rudderow-stewart', 'kathie-sowell', 'kathie-sowell', 'bryan-holocker', 'maraea-ainge-harris', 'kim-vuth', 'katrina-yeaw', 'nina-mcdaniel', 'greg-friedmann', 'greg-friedmann', 'linda-civitello', 'josh-raisher', 'theresa-early', 'jane-twitmyer', 'mia-thrash', 'locket-lane', 'daniel-campbell', 'jeff-seigle', 'juan-fernando-vigil', 'nancy-zanner-correll', 'ruth-blau', 'jeff-spieler', 'bob-allnutt', 'marcelo-raffaelli', 'thomas-megan', 'eamon-harper', 'xiumei-hong', 'greg-luersen', 'beverly-sheng', 'jeffrey-gerarde', 'david-suda', 'angela-marie-fielding', 'kyleigh-lipira', 'shari-ciccotelli', 'emelda-siri-ntinglet', 'katherine-elizabeth-brown', 'zeal-somani', 'karen-linett', 'steve-baddour', 'eduardo-alfondo-campo-vergara', 'charles-holliman', 'gary-voelker', 'diane-rehm', 'francis-nurthen-iii', 'ed-nanas', 'telon-yan', 'tom-brady', 'tom-brady', 'steve-bannon', 'yihung-mohs', 'chuck-woolery', 'elaine-schwartz', 'cliff-doumas', 'cale-jaffe', 'katy-perry', 'von-spakovsky', 'donald-trump', 'donald-trump', 'donald-trump', 'donald-trump', 'donald-trump', 'donald-trump', 'donald-trump', 'donald-trump', 'donald-trump', 'donald-trump', 'donald-trump', 'donald-trump', 'donald-trump', 'donald-trump', 'donald-trump', 'donald-trump', 'donald-trump', 'donald-trump', 'donald-trump', 'bernadette-nakamura', 'robert-andrews', 'richard-stone-rothblum', 'tina-gray', 'damon-greer', 'jim-webb', 'robert-brown', 'robert-pringle', 'john-tani', 'nancy-brouillard-mckenzie', 'nancy-brouillard-mckenzie', 'laurie-goldman', 'blane-morse', 'conrad-charles-daly', 'harold-bergin', 'joanna-clare-solloway', 'tom-foster', 'david-epp', 'christian-hill', 'katie-fineran', 'thornton-davies', 'hannah-mcdonald', 'ricki-crescenzi', 'chang-lin', 'megan-turpin', 'edward-zewicke', 'lee-hwy', 'sarah-slegers', 'brian-lem', 'brian-lem', 'susan-klish', 'hicham-djebbour', 'dave-anderson', 'dennis-clarke', 'kathleen-wood', 'rahm-emanuel', 'peter-lawler', 'yvonne-perret', 'megan-schmitz', 'aaron-steckleberg', 'george-john', 'ted-lopatkiewicz', 'michael-lowe', 'daniel-druckman', 'carl-tobias', 'carl-tobias', 'patrick-phillips', 'matthew-barnes', 'katie-fisher', 'john-gaudette', 'michael-lilek'])
# bad_files = set([u'3321.txt', u'43461.txt', u'8626.txt', u'620.txt', u'33791.txt', u'34614.txt', u'28752.txt', u'46085.txt', u'23277.txt', u'35587.txt', u'46997.txt', u'45878.txt', u'7668.txt', u'19348.txt', u'26734.txt', u'16516.txt', u'47142.txt', u'47448.txt', u'27167.txt', u'46394.txt', u'20924.txt', u'3843.txt', u'35587.txt', u'8528.txt', u'14186.txt', u'20277.txt', u'23670.txt', u'6621.txt', u'58462.txt', u'60163.txt', u'6458.txt', u'46997.txt', u'31151.txt', u'15215.txt', u'48531.txt', u'44360.txt', u'41970.txt', u'21274.txt', u'53693.txt', u'25758.txt', u'6151.txt', u'58492.txt', u'45363.txt', u'18905.txt', u'16.txt', u'23277.txt', u'36447.txt', u'40365.txt', u'22475.txt', u'53191.txt', u'20800.txt', u'34706.txt', u'55106.txt', u'58078.txt', u'11398.txt', u'31362.txt', u'23521.txt', u'35540.txt', u'30807.txt', u'46685.txt', u'8227.txt', u'34376.txt', u'46997.txt', u'30674.txt', u'44771.txt', u'21404.txt', u'50295.txt', u'53403.txt', u'60027.txt', u'19163.txt', u'15191.txt', u'19198.txt', u'14060.txt', u'19072.txt', u'26328.txt', u'3552.txt', u'61087.txt', u'28695.txt', u'60954.txt', u'23709.txt', u'18406.txt', u'20299.txt', u'10877.txt', u'54700.txt', u'33116.txt', u'49935.txt', u'9209.txt', u'9086.txt', u'30263.txt', u'14631.txt', u'14176.txt', u'59011.txt', u'26215.txt', u'29665.txt', u'34670.txt', u'54650.txt', u'37895.txt', u'39395.txt', u'18144.txt', u'46108.txt', u'27202.txt', u'24962.txt', u'50139.txt'])
#print(bad_ne)
printable = set(string.printable)
for named_entity in named_entitys:
    # print(named_entity)
    filenames = ner_location_json[named_entity]
    #filenames = occurances.keys()
    # print(named_entity)
    #print(named_entity)
    named_entity = named_entity.replace(".","")
    # print("REPLACED " + named_entity)
    ner_dash_seperated = named_entity.replace(" ","-")
    #if(ner_dash_seperated not in bad_ne):
    #    continue
    #if named_entity is not 'salvatore j culosi':
    #    continue
    #print(named_entity)
    #ner_doc_count = ner_occurance_count[named_entity]
    for filename in filenames:
        # if filename not in bad_files:
        #     continue
        #ner_forms = sorted(set([s.split('"')[1] for s in occurances[filename]]), key=lambda s:-len(s.split(" ")))
        #print(ner_forms)
        try:
            filename_full = "/Users/jayashree/Documents/NERDEMO_wash/src/final/"+filename.strip()
            f=open(filename_full)
            raw= (f.read())
            raw = filter(lambda x: x in printable, raw)
            tokens = tokenizer.tokenize(raw.lower())
            print(tokens)

            #token_list_reduced = [i for i in tokens if len(i)>2]
            #stopped_tokens = [i for i in token_list_reduced if not i in en_stop]
            #tokens_noise_removed = [i for i in stopped_tokens if i in dictionary_words_set]
            count=1
            token_idxs=set()
            output = extract_words_in_window(tokens,named_entity.lower(),2,token_idxs)
            ner_doc_count = 0
            #print(output)
            for doc in output.documents:
                try:
                    doc_str = " ".join(doc).encode('utf-8').strip()
                    line_in_file = ner_dash_seperated + "&" + filename+ "&" + str(count) + "#" + doc_str
                    name_disambi.write(line_in_file.encode('utf-8')+"\n")

                    # name_disambi.write(ner_dash_seperated + "&" + filename+ "&" + str(count) + "#" + doc_str + "\n")
                    inpt_nameWindow_line = ner_dash_seperated+"#"+doc_str
                    inputForNameWindow.write(inpt_nameWindow_line.encode('utf-8')+"\n")

                    # inputForNameWindow.write(ner_dash_seperated+"#"+doc_str + "\n")
                    # word_vec_line = " ".join(doc[:2]) + " " + named_entity.lower().replace(" ","") + " "+ " ".join(doc[2:]) + " .\n"
                    # wordVec.write(word_vec_line.encode('utf-8')+"\n")
                    #

                    # wordVec.write(" ".join(doc[:2]) + " " + named_entity.lower().replace(" ","") + " "+ " ".join(doc[2:]) + " .\n")
                    count = count + 1
                    ner_doc_count = ner_doc_count - 1
                except Exception as e:
                    print(ner_dash_seperated)
                    print(doc_str)
                    print("Error output.documents: %s" % e)
                    traceback.print_exc()
                    #raise e
            for wv in output.wordvec:
                wordvec_line = " ".join(wv[0]) + " " + named_entity.lower().replace(" ","") + " "+ " ".join(wv[1]) + " ."
                wordVec.write(wordvec_line.encode('utf-8')+"\n")

                # wordVec.write(" ".join(wv[0]) + " " + named_entity.lower().replace(" ","") + " "+ " ".join(wv[1]) + " .\n")
            tokens_idx_to_remove = output.tokens_idx_to_remove
            tokens_idx_to_remove.reverse()
            #for idx in tokens_idx_to_remove:
            #    del tokens[idx]
        except Exception as e:
            print(named_entity)
            print("%s - %s" %(named_entity,filename))
            print("Error filename: %s" % e)
            traceback.print_exc()
            missed_files.append(filename)
            #raise e
        #break
    #ner_occurance_count[named_entity] = ner_doc_count
#print(ner_occurance_count)
print(missed_files)
name_disambi.close()
inputForNameWindow.close()
wordVec.close()
