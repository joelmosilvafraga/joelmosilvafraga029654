--Inserindo os tipos de albuns na tabela album_type
INSERT INTO album_type (code, description) VALUES
  ('STUDIO','Álbum de estúdio'),
  ('LIVE','Álbum ao vivo'),
  ('COMPILATION','Coletânea'),
  ('EP','Extended Play'),
  ('SINGLE','Single')
ON CONFLICT (code) DO UPDATE
SET description = EXCLUDED.description;

--Inserindo os albuns propriamente ditos
INSERT INTO album (title, release_year, album_type_id,genre, created_at, updated_at)
SELECT v.title, v.release_year, t.id , v.genre, now(), now()
FROM (
  VALUES
-- =========================
-- TITÃS (STUDIO)
-- =========================
('Titãs', 1984, 'STUDIO', 'Rock'),
('Televisão', 1985, 'STUDIO', 'Rock'),
('Cabeça Dinossauro', 1986, 'STUDIO', 'Punk Rock'),
('Jesus Não Tem Dentes no País dos Banguelas', 1987, 'STUDIO', 'Rock'),
('Õ Blésq Blom', 1989, 'STUDIO', 'Rock Alternativo'),
('Tudo ao Mesmo Tempo Agora', 1991, 'STUDIO', 'Rock'),
('Titanomaquia', 1993, 'STUDIO', 'Rock Pesado'),
('Domingo', 1995, 'STUDIO', 'Rock Alternativo'),
('Volume Dois', 1998, 'STUDIO', 'Pop Rock'),
('As Dez Mais', 1999, 'STUDIO', 'Pop Rock'),
('A Melhor Banda de Todos os Tempos da Última Semana', 2001, 'STUDIO', 'Rock Alternativo'),
('Como Estão Vocês?', 2003, 'STUDIO', 'Rock'),
('Sacos Plásticos', 2009, 'STUDIO', 'Rock Alternativo'),
('Nheengatu', 2014, 'STUDIO', 'Rock'),
('Doze Flores Amarelas', 2018, 'STUDIO', 'Rock Experimental'),
('Titãs Trio Acústico', 2021, 'STUDIO', 'Acústico'),
('Olho Furta-cor', 2022, 'STUDIO', 'Rock Alternativo'),


  -- =========================
  -- SKANK (álbuns de estúdio)
  -- =========================
('Skank', 1992, 'STUDIO', 'Pop Rock'),
('Calango', 1994, 'STUDIO', 'Reggae Rock'),
('O Samba Poconé', 1996, 'STUDIO', 'Pop Rock'),
('Siderado', 1998, 'STUDIO', 'Britpop'),
('Maquinarama', 2000, 'STUDIO', 'Pop Rock'),
('Cosmotron', 2003, 'STUDIO', 'Rock Alternativo'),
('Carrossel', 2006, 'STUDIO', 'Pop Rock'),
('Estandarte', 2008, 'STUDIO', 'Pop Rock'),
('Velocia', 2014, 'STUDIO', 'Pop Rock'),

  -- ==================================
  -- CAPITAL INICIAL (álbuns de estúdio)
  -- ==================================
('Capital Inicial', 1986, 'STUDIO', 'Rock'),
('Independência', 1987, 'STUDIO', 'Rock'),
('Você não Precisa Entender', 1988, 'STUDIO', 'Rock'),
('Todos os Lados', 1989, 'STUDIO', 'Pop Rock'),
('Eletricidade', 1991, 'STUDIO', 'Hard Rock'),
('Rua 47', 1995, 'STUDIO', 'Rock Alternativo'),
('Atrás dos Olhos', 1998, 'STUDIO', 'Pop Rock'),
('Rosas e Vinho Tinto', 2002, 'STUDIO', 'Pop Rock'),
('Gigante!', 2004, 'STUDIO', 'Rock Alternativo'),
('MTV Especial: Aborto Elétrico', 2005, 'STUDIO', 'Rock'),
('Eu Nunca Disse Adeus', 2007, 'STUDIO', 'Rock'),
('Das Kapital', 2010, 'STUDIO', 'Rock Alternativo'),
('Saturno', 2012, 'STUDIO', 'Rock'),
('Sonora', 2018, 'STUDIO', 'Pop Rock'),

-- =========================
-- OS PARALAMAS DO SUCESSO
-- =========================
('Cinema Mudo', 1983, 'STUDIO', 'Rock'),
('O Passo do Lui', 1984, 'STUDIO', 'Rock'),
('Selvagem?', 1986, 'STUDIO', 'Rock/Reggae'),
('Bora-Bora', 1988, 'STUDIO', 'Rock'),
('Big Bang', 1989, 'STUDIO', 'Pop Rock'),
('Os Grãos', 1991, 'STUDIO', 'Rock Alternativo'),
('Severino', 1994, 'STUDIO', 'Rock Alternativo'),
('Hey Na Na', 1998, 'STUDIO', 'Pop Rock'),
('Longo Caminho', 2002, 'STUDIO', 'Pop Rock'),
('Hoje', 2005, 'STUDIO', 'Rock'),
('Brasil Afora', 2009, 'STUDIO', 'Rock'),
('Sinais do Sim', 2017, 'STUDIO', 'Rock'),

-- =========================
-- JOTA QUEST
-- =========================
('Jota Quest', 1996, 'STUDIO', 'Pop Rock'),
('De Volta ao Planeta', 1998, 'STUDIO', 'Pop Rock/Funk'),
('Oxigênio', 2000, 'STUDIO', 'Pop Rock'),
('Discotecagem Pop Variada', 2002, 'STUDIO', 'Pop Rock'),
('Até Onde Vai', 2005, 'STUDIO', 'Pop Rock'),
('La Plata', 2008, 'STUDIO', 'Pop Rock'),
('Funky Funky Boom Boom', 2013, 'STUDIO', 'Funk Rock'),
('Pancadélico', 2015, 'STUDIO', 'Pop Rock'),

-- =========================
-- CHARLIE BROWN JR
-- =========================
('Transpiração Contínua Prolongada', 1997, 'STUDIO', 'Skate Punk'),
('Preço Curto... Prazo Longo', 1999, 'STUDIO', 'Hardcore/Punk Rock'),
('Nadando com os Tubarões', 2000, 'STUDIO', 'Rap Rock'),
('Bocas Ordinárias', 2002, 'STUDIO', 'Rock Alternativo'),
('Tamo Aí na Atividade', 2004, 'STUDIO', 'Rap Rock'),
('Ritmo, Ritual e Responsa', 2007, 'STUDIO', 'Rock Alternativo'),
('Camisa 10 Joga Bola Até na Chuva', 2009, 'STUDIO', 'Rock'),
('La Familia 013', 2013, 'STUDIO', 'Rock Alternativo'),

-- =========================
-- BARÃO VERMELHO
-- =========================
('Barão Vermelho', 1982, 'STUDIO', 'Rock'),
('Barão Vermelho 2', 1983, 'STUDIO', 'Rock'),
('Maior Abandonado', 1984, 'STUDIO', 'Rock'),
('Declare Guerra', 1986, 'STUDIO', 'Rock'),
('Rock''n Geral', 1987, 'STUDIO', 'Blues Rock'),
('Carnaval', 1988, 'STUDIO', 'Rock'),
('Na Calada da Noite', 1991, 'STUDIO', 'Rock'),
('Carne Crua', 1994, 'STUDIO', 'Rock'),

-- =========================
-- CÁSSIA ELLER
-- =========================
('Cássia Eller', 1990, 'STUDIO', 'Rock/MPB'),
('O Marginal', 1992, 'STUDIO', 'Rock'),
('Cássia Eller (1994)', 1994, 'STUDIO', 'Rock'),
('Veneno Antimonotonia', 1997, 'STUDIO', 'Rock Alternativo'),
('Com Você... Meu Mundo Ficaria Completo', 1999, 'STUDIO', 'MPB/Rock'),

-- =========================
-- MARISA MONTE
-- =========================
('Mais', 1991, 'STUDIO', 'MPB'),
('Verde, Anil, Amarelo, Cor-de-Rosa e Carvão', 1994, 'STUDIO', 'MPB/Pop'),
('Barulhinho Bom', 1996, 'STUDIO', 'MPB/Samba'),
('Memórias, Crônicas e Declarações de Amor', 2000, 'STUDIO', 'MPB/Pop'),
('Universo ao Meu Redor', 2006, 'STUDIO', 'Samba/MPB'),
('Infinito Particular', 2006, 'STUDIO', 'MPB/Pop'),
('O Que Você Quer Saber de Verdade', 2011, 'STUDIO', 'MPB'),
('Portas', 2021, 'STUDIO', 'MPB/Indie'),

-- =========================
-- GILBERTO GIL
-- =========================
('Louvação', 1967, 'STUDIO', 'MPB'),
('Gilberto Gil', 1968, 'STUDIO', 'Tropicália'),
('Refazenda', 1975, 'STUDIO', 'MPB/Folk'),
('Realce', 1979, 'STUDIO', 'Disco/Funk'),
('Raça Humana', 1984, 'STUDIO', 'MPB/Pop'),
('Quanta', 1997, 'STUDIO', 'MPB/Experimental'),
('Ok Ok Ok', 2018, 'STUDIO', 'MPB'),

-- =========================
-- CAETANO VELOSO
-- =========================
('Caetano Veloso', 1968, 'STUDIO', 'Tropicália'),
('Tropicália ou Panis et Circencis', 1968, 'STUDIO', 'Tropicália'),
('Transa', 1972, 'STUDIO', 'MPB/Rock'),
('Bicho', 1977, 'STUDIO', 'MPB'),
('Estrangeiro', 1989, 'STUDIO', 'MPB/Alternativo'),
('Livro', 1997, 'STUDIO', 'MPB'),
('Meu Coco', 2021, 'STUDIO', 'MPB'),

-- =========================
-- LULU SANTOS
-- =========================
('Tempos Modernos', 1982, 'STUDIO', 'Pop Rock'),
('O Ritmo do Momento', 1983, 'STUDIO', 'Pop Rock'),
('Tudo Azul', 1984, 'STUDIO', 'Pop Rock'),
('Normal', 1985, 'STUDIO', 'Pop Rock'),
('Toda Forma de Amor', 1988, 'STUDIO', 'Pop Rock'),
('Liga Lá', 1997, 'STUDIO', 'Pop Rock'),
('Baby Baby!', 2010, 'STUDIO', 'Pop Rock'),


-- =========================
-- METALLICA
-- =========================
('Kill ''Em All',1983,'STUDIO','Thrash Metal'),
('Ride the Lightning',1984,'STUDIO','Thrash Metal'),
('Master of Puppets',1986,'STUDIO','Thrash Metal'),
('...And Justice for All',1988,'STUDIO','Thrash Metal'),
('Metallica (Black Album)',1991,'STUDIO','Heavy Metal'),
('Load',1996,'STUDIO','Hard Rock'),
('Reload',1997,'STUDIO','Hard Rock'),
('St. Anger',2003,'STUDIO','Nu Metal'),
('Death Magnetic',2008,'STUDIO','Thrash Metal'),
('Hardwired... to Self-Destruct',2016,'STUDIO','Heavy Metal'),
('72 Seasons',2023,'STUDIO','Heavy Metal'),

-- =========================
-- NIRVANA
-- =========================
('Bleach',1989,'STUDIO','Grunge'),
('Nevermind',1991,'STUDIO','Grunge'),
('In Utero',1993,'STUDIO','Grunge'),

-- =========================
-- PEARL JAM
-- =========================
('Ten',1991,'STUDIO','Grunge'),
('Vs.',1993,'STUDIO','Grunge'),
('Vitalogy',1994,'STUDIO','Alternative Rock'),
('No Code',1996,'STUDIO','Alternative Rock'),
('Yield',1998,'STUDIO','Alternative Rock'),
('Binaural',2000,'STUDIO','Alternative Rock'),
('Riot Act',2002,'STUDIO','Alternative Rock'),
('Pearl Jam',2006,'STUDIO','Rock'),
('Backspacer',2009,'STUDIO','Rock'),
('Lightning Bolt',2013,'STUDIO','Rock'),
('Gigaton',2020,'STUDIO','Alternative Rock'),

-- =========================
-- RED HOT CHILI PEPPERS
-- =========================
('The Red Hot Chili Peppers',1984,'STUDIO','Funk Rock'),
('Freaky Styley',1985,'STUDIO','Funk Rock'),
('The Uplift Mofo Party Plan',1987,'STUDIO','Funk Rock'),
('Mother''s Milk',1989,'STUDIO','Funk Metal'),
('Blood Sugar Sex Magik',1991,'STUDIO','Funk Rock'),
('Californication',1999,'STUDIO','Alternative Rock'),
('By the Way',2002,'STUDIO','Alternative Rock'),
('Stadium Arcadium',2006,'STUDIO','Alternative Rock'),
('I''m with You',2011,'STUDIO','Alternative Rock'),
('The Getaway',2016,'STUDIO','Alternative Rock'),
('Unlimited Love',2022,'STUDIO','Alternative Rock'),

-- =========================
-- LINKIN PARK
-- =========================
('Hybrid Theory',2000,'STUDIO','Nu Metal'),
('Meteora',2003,'STUDIO','Nu Metal'),
('Minutes to Midnight',2007,'STUDIO','Alternative Rock'),
('A Thousand Suns',2010,'STUDIO','Electronic Rock'),
('Living Things',2012,'STUDIO','Electronic Rock'),
('The Hunting Party',2014,'STUDIO','Hard Rock'),
('One More Light',2017,'STUDIO','Pop Rock'),

-- =========================
-- COLDPLAY
-- =========================
('Parachutes',2000,'STUDIO','Alternative Rock'),
('A Rush of Blood to the Head',2002,'STUDIO','Alternative Rock'),
('X&Y',2005,'STUDIO','Alternative Rock'),
('Viva la Vida or Death and All His Friends',2008,'STUDIO','Alternative Rock'),
('Mylo Xyloto',2011,'STUDIO','Pop Rock'),
('Ghost Stories',2014,'STUDIO','Ambient Pop'),
('A Head Full of Dreams',2015,'STUDIO','Pop Rock'),
('Everyday Life',2019,'STUDIO','Alternative Rock'),
('Music of the Spheres',2021,'STUDIO','Pop'),

-- =========================
-- U2
-- =========================
('Boy',1980,'STUDIO','Post-Punk'),
('October',1981,'STUDIO','Post-Punk'),
('War',1983,'STUDIO','Rock'),
('The Unforgettable Fire',1984,'STUDIO','Alternative Rock'),
('The Joshua Tree',1987,'STUDIO','Rock'),
('Achtung Baby',1991,'STUDIO','Alternative Rock'),
('Zooropa',1993,'STUDIO','Electronic Rock'),
('Pop',1997,'STUDIO','Pop Rock'),
('All That You Can''t Leave Behind',2000,'STUDIO','Rock'),
('How to Dismantle an Atomic Bomb',2004,'STUDIO','Rock'),
('Songs of Innocence',2014,'STUDIO','Rock'),
('Songs of Experience',2017,'STUDIO','Rock'),

-- =========================
-- QUEEN
-- =========================
('Queen',1973,'STUDIO','Hard Rock'),
('Queen II',1974,'STUDIO','Progressive Rock'),
('Sheer Heart Attack',1974,'STUDIO','Hard Rock'),
('A Night at the Opera',1975,'STUDIO','Rock'),
('A Day at the Races',1976,'STUDIO','Rock'),
('News of the World',1977,'STUDIO','Rock'),
('Jazz',1978,'STUDIO','Rock'),
('The Game',1980,'STUDIO','Rock/Pop'),
('Hot Space',1982,'STUDIO','Pop Rock'),
('The Works',1984,'STUDIO','Rock'),
('A Kind of Magic',1986,'STUDIO','Rock'),
('The Miracle',1989,'STUDIO','Rock'),
('Innuendo',1991,'STUDIO','Rock'),
('Made in Heaven',1995,'STUDIO','Rock'),

-- =========================
-- THE BEATLES
-- =========================
('Please Please Me',1963,'STUDIO','Rock and Roll'),
('With the Beatles',1963,'STUDIO','Rock and Roll'),
('A Hard Day''s Night',1964,'STUDIO','Pop Rock'),
('Beatles for Sale',1964,'STUDIO','Rock'),
('Help!',1965,'STUDIO','Pop Rock'),
('Rubber Soul',1965,'STUDIO','Folk Rock'),
('Revolver',1966,'STUDIO','Psychedelic Rock'),
('Sgt. Pepper''s Lonely Hearts Club Band',1967,'STUDIO','Psychedelic Rock'),
('The Beatles (White Album)',1968,'STUDIO','Rock'),
('Abbey Road',1969,'STUDIO','Rock'),
('Let It Be',1970,'STUDIO','Rock'),

-- =========================
-- PINK FLOYD
-- =========================
('The Piper at the Gates of Dawn',1967,'STUDIO','Psychedelic Rock'),
('A Saucerful of Secrets',1968,'STUDIO','Psychedelic Rock'),
('More',1969,'STUDIO','Progressive Rock'),
('Ummagumma',1969,'STUDIO','Progressive Rock'),
('Atom Heart Mother',1970,'STUDIO','Progressive Rock'),
('Meddle',1971,'STUDIO','Progressive Rock'),
('The Dark Side of the Moon',1973,'STUDIO','Progressive Rock'),
('Wish You Were Here',1975,'STUDIO','Progressive Rock'),
('Animals',1977,'STUDIO','Progressive Rock'),
('The Wall',1979,'STUDIO','Progressive Rock'),
('The Final Cut',1983,'STUDIO','Progressive Rock'),
('A Momentary Lapse of Reason',1987,'STUDIO','Progressive Rock'),
('The Division Bell',1994,'STUDIO','Progressive Rock'),

-- =========================
-- AC/DC
-- =========================
('High Voltage',1975,'STUDIO','Hard Rock'),
('T.N.T.',1975,'STUDIO','Hard Rock'),
('Dirty Deeds Done Dirt Cheap',1976,'STUDIO','Hard Rock'),
('Let There Be Rock',1977,'STUDIO','Hard Rock'),
('Powerage',1978,'STUDIO','Hard Rock'),
('Highway to Hell',1979,'STUDIO','Hard Rock'),
('Back in Black',1980,'STUDIO','Hard Rock'),
('For Those About to Rock We Salute You',1981,'STUDIO','Hard Rock'),
('The Razors Edge',1990,'STUDIO','Hard Rock'),
('Ballbreaker',1995,'STUDIO','Hard Rock'),
('Stiff Upper Lip',2000,'STUDIO','Hard Rock'),
('Black Ice',2008,'STUDIO','Hard Rock'),
('Rock or Bust',2014,'STUDIO','Hard Rock'),
('Power Up',2020,'STUDIO','Hard Rock'),

-- =========================
-- IMAGINE DRAGONS
-- =========================
('Night Visions',2012,'STUDIO','Pop Rock'),
('Smoke + Mirrors',2015,'STUDIO','Alternative Rock'),
('Evolve',2017,'STUDIO','Pop Rock'),
('Origins',2018,'STUDIO','Pop Rock'),
('Mercury – Act 1',2021,'STUDIO','Alternative Rock'),
('Mercury – Act 2',2022,'STUDIO','Alternative Rock'),
('Loom',2024,'STUDIO','Pop Rock'),

-- =========================
-- ARCTIC MONKEYS
-- =========================
('Whatever People Say I Am, That''s What I''m Not',2006,'STUDIO','Indie Rock'),
('Favourite Worst Nightmare',2007,'STUDIO','Indie Rock'),
('Humbug',2009,'STUDIO','Alternative Rock'),
('Suck It and See',2011,'STUDIO','Indie Rock'),
('AM',2013,'STUDIO','Alternative Rock'),
('Tranquility Base Hotel & Casino',2018,'STUDIO','Art Rock'),
('The Car',2022,'STUDIO','Art Rock'),



-- =========================
-- BON JOVI (STUDIO)
-- =========================
('Bon Jovi',1984,'STUDIO','Hard Rock'),
('7800° Fahrenheit',1985,'STUDIO','Hard Rock'),
('Slippery When Wet',1986,'STUDIO','Hard Rock'),
('New Jersey',1988,'STUDIO','Hard Rock'),
('Keep the Faith',1992,'STUDIO','Hard Rock'),
('These Days',1995,'STUDIO','Hard Rock'),
('Crush',2000,'STUDIO','Pop Rock'),
('Bounce',2002,'STUDIO','Hard Rock'),
('Have a Nice Day',2005,'STUDIO','Pop Rock'),
('Lost Highway',2007,'STUDIO','Country Rock'),
('The Circle',2009,'STUDIO','Rock'),
('What About Now',2013,'STUDIO','Pop Rock'),
('This House Is Not for Sale',2016,'STUDIO','Rock'),
('2020',2020,'STUDIO','Rock'),
('Forever',2024,'STUDIO','Rock'),

-- =========================
-- EVANESCENCE (STUDIO)
-- =========================
('Fallen',2003,'STUDIO','Gothic Rock'),
('The Open Door',2006,'STUDIO','Alternative Metal'),
('Evanescence',2011,'STUDIO','Alternative Rock'),
('The Bitter Truth',2021,'STUDIO','Alternative Metal'),

 -- =========================
 -- MICHEL TELÓ (STUDIO)
 -- =========================
 ('Balada Sertaneja', 2009, 'STUDIO', 'Sertanejo Universitário'),

 -- =========================
 -- GUNS N'' ROSES (STUDIO)
 -- =========================
 ('Appetite for Destruction', 1987, 'STUDIO', 'Hard Rock'),
 ('G N'' R Lies', 1988, 'STUDIO', 'Hard Rock'),
 ('Use Your Illusion I', 1991, 'STUDIO', 'Hard Rock'),
 ('Use Your Illusion II', 1991, 'STUDIO', 'Hard Rock'),
 ('The Spaghetti Incident?', 1993, 'STUDIO', 'Hard Rock'),
 ('Chinese Democracy', 2008, 'STUDIO', 'Alternative Rock'),

 -- =========================
 -- MIKE SHINODA (STUDIO)
 -- =========================
 ('Post Traumatic', 2018, 'STUDIO', 'Alternative Hip Hop'),
 ('Dropped Frames, Vol. 1', 2020, 'STUDIO', 'Electronic'),
 ('Dropped Frames, Vol. 2', 2020, 'STUDIO', 'Electronic'),
 ('Dropped Frames, Vol. 3', 2020, 'STUDIO', 'Electronic'),

 -- =========================
 -- STEREOPHONICS (STUDIO)
 -- =========================
 ('Word Gets Around', 1997, 'STUDIO', 'Britpop'),
 ('Performance and Cocktails', 1999, 'STUDIO', 'Britpop'),
 ('Just Enough Education to Perform', 2001, 'STUDIO', 'Alternative Rock'),
 ('You Gotta Go There to Come Back', 2003, 'STUDIO', 'Alternative Rock'),
 ('Language. Sex. Violence. Other?', 2005, 'STUDIO', 'Hard Rock'),
 ('Pull the Pin', 2007, 'STUDIO', 'Alternative Rock'),
 ('Keep Calm and Carry On', 2009, 'STUDIO', 'Pop Rock'),
 ('Graffiti on the Train', 2013, 'STUDIO', 'Alternative Rock'),
 ('Keep the Village Alive', 2015, 'STUDIO', 'Rock'),
 ('Scream Above the Sounds', 2017, 'STUDIO', 'Rock'),
 ('Kind', 2019, 'STUDIO', 'Folk Rock'),
 ('Oochya!', 2022, 'STUDIO', 'Rock'),
 ('Make ''Em Laugh, Make ''Em Cry, Make ''Em Wait', 2025, 'STUDIO', 'Rock'),

 -- =========================
 -- SERJ TANKIAN (STUDIO)
 -- =========================
 ('Elect the Dead', 2007, 'STUDIO', 'Alternative Metal'),
 ('Imperfect Harmonies', 2010, 'STUDIO', 'Experimental Rock'),
 ('Harakiri', 2012, 'STUDIO', 'Alternative Metal'),
 ('Orca', 2013, 'STUDIO', 'Orchestral/Experimental'),
 ('Covers, Collaborations & Collages', 2025, 'STUDIO', 'Experimental Rock'),

 -- =========================
 -- DJAVAN (STUDIO)
 -- =========================
 ('A Voz, o Violão, a Música de Djavan', 1976, 'STUDIO', 'MPB'),
 ('Djavan', 1979, 'STUDIO', 'MPB'),
 ('Alumbramento', 1980, 'STUDIO', 'MPB'),
 ('Seduzir', 1981, 'STUDIO', 'MPB'),
 ('Luz', 1982, 'STUDIO', 'MPB'),
 ('Lilás', 1984, 'STUDIO', 'MPB'),
 ('Meu Lado', 1986, 'STUDIO', 'MPB'),
 ('Não É Azul, mas É Mar', 1987, 'STUDIO', 'MPB'),
 ('Djavan (1989)', 1989, 'STUDIO', 'MPB'),
 ('Coisa de Acender', 1992, 'STUDIO', 'MPB'),
 ('Novena', 1994, 'STUDIO', 'MPB'),
 ('Malásia', 1996, 'STUDIO', 'MPB'),
 ('Bicho Solto', 1998, 'STUDIO', 'MPB'),
 ('Milagreiro', 2001, 'STUDIO', 'MPB'),
 ('Vaidade', 2004, 'STUDIO', 'MPB'),
 ('Matizes', 2007, 'STUDIO', 'MPB'),
 ('Ária', 2010, 'STUDIO', 'MPB/Clássico'),
 ('Rua dos Amores', 2012, 'STUDIO', 'MPB'),
 ('Vidas pra Contar', 2015, 'STUDIO', 'MPB'),
 ('Vesúvio', 2018, 'STUDIO', 'MPB'),
 ('D', 2022, 'STUDIO', 'MPB'),
 ('Origem', 2024, 'STUDIO', 'MPB'),


-- =========================
-- MICHEL TELÓ
-- =========================
('Michel Teló – Ao Vivo', 2012, 'LIVE', 'Sertanejo Universitário'),
('Bem Sertanejo – O Musical (Ao Vivo)', 2017, 'LIVE', 'Sertanejo'),

-- =========================
-- GUNS N'' ROSES
-- =========================
('Live Era ''87–''93', 1999, 'LIVE', 'Hard Rock'),

-- =========================
-- MIKE SHINODA
-- =========================
('Live in London', 2019, 'LIVE', 'Alternative Hip Hop'),

-- =========================
-- STEREOPHONICS
-- =========================
('Live from Dakota', 2006, 'LIVE', 'Alternative Rock'),
('Decade in the Sun: Live at Cardiff Castle', 2008, 'LIVE', 'Alternative Rock'),

-- =========================
-- SERJ TANKIAN
-- =========================
('Elect the Dead Symphony (Live with Orchestra)', 2010, 'LIVE', 'Orchestral Rock'),

-- =========================
-- DJAVAN
-- =========================
('Djavan Ao Vivo', 1999, 'LIVE', 'MPB'),
('Multishow Ao Vivo – Djavan', 2011, 'LIVE', 'MPB'),
('Voz & Violão – Ao Vivo', 2015, 'LIVE', 'MPB/Acústico'),

-- =========================
-- METALLICA
-- =========================
('Live Shit: Binge & Purge', 1993, 'LIVE', 'Heavy Metal'),
('S&M', 1999, 'LIVE', 'Symphonic Metal'),
('Orgullo, Pasión y Gloria: Tres Noches en la Ciudad de México', 2009, 'LIVE', 'Heavy Metal'),
('Through the Never (Live)', 2013, 'LIVE', 'Heavy Metal'),
('S&M2', 2020, 'LIVE', 'Symphonic Metal'),

-- =========================
-- NIRVANA
-- =========================
('MTV Unplugged in New York', 1994, 'LIVE', 'Acoustic Rock'),
('From the Muddy Banks of the Wishkah', 1996, 'LIVE', 'Grunge'),
('Live at Reading', 2009, 'LIVE', 'Grunge'),

-- =========================
-- PEARL JAM
-- =========================
('Live on Two Legs', 1998, 'LIVE', 'Alternative Rock'),
('Live at the Gorge 05/06', 2007, 'LIVE', 'Alternative Rock'),
('Let’s Play Two', 2017, 'LIVE', 'Rock'),
('Gigaton Tour Live', 2022, 'LIVE', 'Alternative Rock'),

-- =========================
-- RED HOT CHILI PEPPERS
-- =========================
('Live in Hyde Park', 2004, 'LIVE', 'Funk Rock'),

-- =========================
-- LINKIN PARK
-- =========================
('Live in Texas', 2003, 'LIVE', 'Nu Metal'),
('Road to Revolution: Live at Milton Keynes', 2008, 'LIVE', 'Alternative Rock'),
('One More Light Live', 2017, 'LIVE', 'Pop Rock'),

-- =========================
-- COLDPLAY
-- =========================
('Live 2003', 2003, 'LIVE', 'Alternative Rock'),
('LeftRightLeftRightLeft', 2009, 'LIVE', 'Alternative Rock'),
('Ghost Stories Live 2014', 2014, 'LIVE', 'Ambient Pop'),

-- =========================
-- U2
-- =========================
('Under a Blood Red Sky', 1983, 'LIVE', 'Rock'),
('Rattle and Hum', 1988, 'LIVE', 'Rock'),
('Zoo TV Live', 1993, 'LIVE', 'Alternative Rock'),
('Live from Paris', 2007, 'LIVE', 'Rock'),

-- =========================
-- QUEEN
-- =========================
('Live Killers', 1979, 'LIVE', 'Rock'),
('Live Magic', 1986, 'LIVE', 'Rock'),
('Live at Wembley ''86', 1992, 'LIVE', 'Rock'),
('Queen Rock Montreal', 2007, 'LIVE', 'Rock'),

-- =========================
-- THE BEATLES
-- =========================
('Live at the Hollywood Bowl', 1977, 'LIVE', 'Rock and Roll'),

-- =========================
-- PINK FLOYD
-- =========================
('Delicate Sound of Thunder', 1988, 'LIVE', 'Progressive Rock'),
('Pulse', 1995, 'LIVE', 'Progressive Rock'),

-- =========================
-- AC/DC
-- =========================
('If You Want Blood You''ve Got It', 1978, 'LIVE', 'Hard Rock'),
('AC/DC Live', 1992, 'LIVE', 'Hard Rock'),
('Live at River Plate', 2012, 'LIVE', 'Hard Rock'),

-- =========================
-- IMAGINE DRAGONS
-- =========================
('Live at AllSaints Studios', 2013, 'LIVE', 'Pop Rock'),

-- =========================
-- ARCTIC MONKEYS
-- =========================
('Live at the Royal Albert Hall', 2020, 'LIVE', 'Alternative Rock'),
-- =========================
-- METALLICA
-- =========================
('Enter Sandman', 1991, 'SINGLE', 'Heavy Metal'),
('Nothing Else Matters', 1992, 'SINGLE', 'Heavy Metal'),
('The Unforgiven', 1991, 'SINGLE', 'Heavy Metal'),
('One', 1989, 'SINGLE', 'Thrash Metal'),
('Master of Puppets', 1986, 'SINGLE', 'Thrash Metal'),

-- =========================
-- NIRVANA
-- =========================
('Smells Like Teen Spirit', 1991, 'SINGLE', 'Grunge'),
('Come as You Are', 1992, 'SINGLE', 'Grunge'),
('Lithium', 1992, 'SINGLE', 'Grunge'),
('In Bloom', 1992, 'SINGLE', 'Grunge'),
('Heart-Shaped Box', 1993, 'SINGLE', 'Grunge'),

-- =========================
-- PEARL JAM
-- =========================
('Alive', 1991, 'SINGLE', 'Grunge'),
('Jeremy', 1992, 'SINGLE', 'Grunge'),
('Even Flow', 1992, 'SINGLE', 'Grunge'),
('Black', 1991, 'SINGLE', 'Alternative Rock'),
('Better Man', 1995, 'SINGLE', 'Rock'),

-- =========================
-- RED HOT CHILI PEPPERS
-- =========================
('Under the Bridge', 1992, 'SINGLE', 'Alternative Rock'),
('Give It Away', 1991, 'SINGLE', 'Funk Rock'),
('Californication', 2000, 'SINGLE', 'Alternative Rock'),
('Scar Tissue', 1999, 'SINGLE', 'Alternative Rock'),
('Can''t Stop', 2002, 'SINGLE', 'Funk Rock'),

-- =========================
-- LINKIN PARK
-- =========================
('In the End', 2001, 'SINGLE', 'Nu Metal'),
('Numb', 2003, 'SINGLE', 'Alternative Rock'),
('Crawling', 2001, 'SINGLE', 'Nu Metal'),
('Somewhere I Belong', 2003, 'SINGLE', 'Nu Metal'),
('What I''ve Done', 2007, 'SINGLE', 'Alternative Rock'),

-- =========================
-- COLDPLAY
-- =========================
('Yellow', 2000, 'SINGLE', 'Alternative Rock'),
('The Scientist', 2002, 'SINGLE', 'Alternative Rock'),
('Clocks', 2002, 'SINGLE', 'Alternative Rock'),
('Viva la Vida', 2008, 'SINGLE', 'Alternative Rock'),
('Fix You', 2005, 'SINGLE', 'Alternative Rock'),

-- =========================
-- U2
-- =========================
('With or Without You', 1987, 'SINGLE', 'Rock'),
('One', 1992, 'SINGLE', 'Alternative Rock'),
('Beautiful Day', 2000, 'SINGLE', 'Rock'),
('Sunday Bloody Sunday', 1983, 'SINGLE', 'Rock'),
('I Still Haven''t Found What I''m Looking For', 1987, 'SINGLE', 'Rock'),

-- =========================
-- QUEEN
-- =========================
('Bohemian Rhapsody', 1975, 'SINGLE', 'Rock'),
('We Will Rock You', 1977, 'SINGLE', 'Rock'),
('We Are the Champions', 1977, 'SINGLE', 'Rock'),
('Another One Bites the Dust', 1980, 'SINGLE', 'Rock'),
('Don''t Stop Me Now', 1979, 'SINGLE', 'Rock'),

-- =========================
-- THE BEATLES
-- =========================
('Hey Jude', 1968, 'SINGLE', 'Rock'),
('Let It Be', 1970, 'SINGLE', 'Rock'),
('Come Together', 1969, 'SINGLE', 'Rock'),
('Help!', 1965, 'SINGLE', 'Pop Rock'),
('I Want to Hold Your Hand', 1963, 'SINGLE', 'Rock and Roll'),

-- =========================
-- PINK FLOYD
-- =========================
('Another Brick in the Wall (Part 2)', 1979, 'SINGLE', 'Progressive Rock'),
('Money', 1973, 'SINGLE', 'Progressive Rock'),
('Wish You Were Here', 1975, 'SINGLE', 'Progressive Rock'),
('Time', 1973, 'SINGLE', 'Progressive Rock'),
('Comfortably Numb', 1979, 'SINGLE', 'Progressive Rock'),

-- =========================
-- AC/DC
-- =========================
('Highway to Hell', 1979, 'SINGLE', 'Hard Rock'),
('Back in Black', 1980, 'SINGLE', 'Hard Rock'),
('Thunderstruck', 1990, 'SINGLE', 'Hard Rock'),
('You Shook Me All Night Long', 1980, 'SINGLE', 'Hard Rock'),
('T.N.T.', 1975, 'SINGLE', 'Hard Rock'),

-- =========================
-- IMAGINE DRAGONS
-- =========================
('Radioactive', 2012, 'SINGLE', 'Pop Rock'),
('Demons', 2013, 'SINGLE', 'Pop Rock'),
('Believer', 2017, 'SINGLE', 'Pop Rock'),
('Thunder', 2017, 'SINGLE', 'Pop Rock'),
('Whatever It Takes', 2017, 'SINGLE', 'Pop Rock'),

-- =========================
-- ARCTIC MONKEYS
-- =========================
('I Bet You Look Good on the Dancefloor', 2005, 'SINGLE', 'Indie Rock'),
('When the Sun Goes Down', 2006, 'SINGLE', 'Indie Rock'),
('Do I Wanna Know?', 2013, 'SINGLE', 'Alternative Rock'),
('R U Mine?', 2012, 'SINGLE', 'Alternative Rock'),
('505', 2007, 'SINGLE', 'Indie Rock'),
-- =========================
-- METALLICA
-- =========================
('The $5.98 E.P. – Garage Days Re-Revisited', 1987, 'EP', 'Thrash Metal'),
('Beyond Magnetic', 2011, 'EP', 'Heavy Metal'),

-- =========================
-- NIRVANA
-- =========================
('Hormoaning', 1992, 'EP', 'Grunge'),

-- =========================
-- RED HOT CHILI PEPPERS
-- =========================
('Rock & Roll Hall of Fame Covers EP', 2012, 'EP', 'Funk Rock'),

-- =========================
-- LINKIN PARK
-- =========================
('Collision Course (with Jay-Z)', 2004, 'EP', 'Rap Rock'),

-- =========================
-- COLDPLAY
-- =========================
('Safety EP', 1998, 'EP', 'Alternative Rock'),
('The Blue Room', 1999, 'EP', 'Alternative Rock'),
('Prospekt’s March', 2008, 'EP', 'Alternative Rock'),

-- =========================
-- U2
-- =========================
('Wide Awake in America', 1985, 'EP', 'Rock'),

-- =========================
-- THE BEATLES
-- =========================
('Magical Mystery Tour (EP original UK)', 1967, 'EP', 'Psychedelic Rock'),



-- =========================
-- IMAGINE DRAGONS
-- =========================
('Imagine Dragons EP', 2009, 'EP', 'Alternative Rock'),
('Hell and Silence', 2010, 'EP', 'Alternative Rock'),
('It’s Time', 2011, 'EP', 'Alternative Rock'),

-- =========================
-- ARCTIC MONKEYS
-- =========================
('Five Minutes with Arctic Monkeys', 2005, 'EP', 'Indie Rock'),
('Who the F*** Are Arctic Monkeys?', 2006, 'EP', 'Indie Rock')



) AS v(title, release_year, album_type_id, genre)
JOIN album_type t ON t.code = v.album_type_id;


INSERT INTO album (title, release_year, album_type_id, genre, created_at, updated_at)
SELECT v.title, v.release_year, t.id, v.genre, now(), now()
FROM (
  VALUES

  -- =========================
  -- NIRVANA
  -- =========================
  ('Incesticide', 1992, 'COMPILATION', 'Grunge'),
  ('Nirvana', 2002, 'COMPILATION', 'Grunge'),
  ('Sliver: The Best of the Box', 2005, 'COMPILATION', 'Grunge'),

  -- =========================
  -- PEARL JAM
  -- =========================
  ('Lost Dogs', 2003, 'COMPILATION', 'Alternative Rock'),
  ('rearviewmirror (greatest hits 1991–2003)', 2004, 'COMPILATION', 'Alternative Rock'),

  -- =========================
  -- RED HOT CHILI PEPPERS
  -- =========================
  ('What Hits!?', 1992, 'COMPILATION', 'Funk Rock'),
  ('Greatest Hits', 2003, 'COMPILATION', 'Alternative Rock'),

  -- =========================
  -- LINKIN PARK
  -- =========================
  ('Papercuts – Singles Collection 2000–2023', 2024, 'COMPILATION', 'Alternative Rock'),

  -- =========================
  -- COLDPLAY
  -- =========================
  ('Coldplay: Reimagined', 2017, 'COMPILATION', 'Alternative Rock'),

  -- =========================
  -- U2
  -- =========================
  ('The Best of 1980–1990', 1998, 'COMPILATION', 'Rock'),
  ('The Best of 1990–2000', 2002, 'COMPILATION', 'Rock'),
  ('U218 Singles', 2006, 'COMPILATION', 'Rock'),

  -- =========================
  -- QUEEN
  -- =========================
  ('Greatest Hits', 1981, 'COMPILATION', 'Rock'),
  ('Greatest Hits II', 1991, 'COMPILATION', 'Rock'),
  ('Greatest Hits III', 1999, 'COMPILATION', 'Rock'),
  ('Absolute Greatest', 2009, 'COMPILATION', 'Rock'),

  -- =========================
  -- THE BEATLES
  -- =========================
  ('1962–1966', 1973, 'COMPILATION', 'Rock'),
  ('1967–1970', 1973, 'COMPILATION', 'Rock'),
  ('Anthology 1', 1995, 'COMPILATION', 'Rock'),
  ('Anthology 2', 1996, 'COMPILATION', 'Rock'),
  ('Anthology 3', 1996, 'COMPILATION', 'Rock'),
  ('1', 2000, 'COMPILATION', 'Rock'),

  -- =========================
  -- PINK FLOYD
  -- =========================
  ('Relics', 1971, 'COMPILATION', 'Progressive Rock'),
  ('A Collection of Great Dance Songs', 1981, 'COMPILATION', 'Progressive Rock'),
  ('Works', 1983, 'COMPILATION', 'Progressive Rock'),
  ('Echoes: The Best of Pink Floyd', 2001, 'COMPILATION', 'Progressive Rock'),

  -- =========================
  -- AC/DC
  -- =========================
  ('Who Made Who', 1986, 'COMPILATION', 'Hard Rock'),
  ('Bonfire', 1997, 'COMPILATION', 'Hard Rock'),
  ('Backtracks', 2009, 'COMPILATION', 'Hard Rock')

) AS v(title, release_year, type_code, genre)
JOIN album_type t ON t.code = v.type_code