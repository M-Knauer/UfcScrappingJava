package com.marcelo.main.scrapping;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.marcelo.main.model.Corner;
import com.marcelo.main.model.Fighter;

public class UfcScrapping2 {
	/*
	private static String getUrlString() {
		try {
			// Pegando o link da pagina da luta
			WebClient webClient = new WebClient(BrowserVersion.CHROME);
			webClient.getOptions().setCssEnabled(false);
			webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
			webClient.getOptions().setThrowExceptionOnScriptError(false);
			webClient.getOptions().setPrintContentOnFailingStatusCode(false);
			
			HtmlPage page;
			page = webClient.getPage("https://www.sherdog.com/events");
			
			
			List<?> btnAnchor = page.getByXPath(
					"/html/body/div[4]/div[3]/div[2]/div[3]/div[3]/a[1]");
			HtmlAnchor link = (HtmlAnchor) btnAnchor.get(0);
			String url = "https://www.sherdog.com" + link.getHrefAttribute();
			
			webClient.close();
			return url;

		} catch (FailingHttpStatusCodeException | IOException e) {
			e.printStackTrace();
			return null;
		}

	}
	*/
	
	private static Document connectJsoup(String url) {
		// conecta na url obtida
		try {
			Document doc = Jsoup.connect(url).get();
			return doc;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private static List<Element> getFightersAnchor(List<Element> fights) {
		// pega as anchors de todos os lutadores
		List<Element> redFightersAnchors = new ArrayList<>();
		List<Element> blueFightersAnchors = new ArrayList<>();
		List<Element> fightersAnchors = new ArrayList<>();
		
		fights.forEach(f -> {
			redFightersAnchors.addAll(f.selectXpath("//div[@class='fighter_list left']/div/a"));
			blueFightersAnchors.addAll(f.selectXpath("//div[@class='fighter_list right']/div/a"));
						
		});
		
		for (int i = 0; i < redFightersAnchors.size(); i++) {
			fightersAnchors.add(redFightersAnchors.get(i));
			fightersAnchors.add(blueFightersAnchors.get(i));
		}
		
		return fightersAnchors;
	}
	
	// pega o link dos lutadores
	private static List<String> getFighterLink(List<Element> fights) {
		
		List<Element> fightersAnchor = getFightersAnchor(fights);
		List<String> fightersLink = new ArrayList<>();
		// pega o link dos lutadores principais
		fights.forEach(f -> {
			fightersLink.add("https://www.sherdog.com" + f.selectXpath("//div[@class='fighter left_side']/h3/a").first().attr("href"));
			fightersLink.add("https://www.sherdog.com" + f.selectXpath("//div[@class='fighter right_side']/h3/a").first().attr("href"));
		});
		
		// pega o link dos demais lutadores
		fightersAnchor.forEach(f -> {
			fightersLink.add("https://www.sherdog.com" + f.attr("href"));
		});
		
		return fightersLink;
		
	}
	
	// pega o nome de todos os lutadores do card
	private static List<String> getFightersName(List<Element> fights) {	
		// pega o nome dos lutadores da luta principal
		List<String> fightersName = new ArrayList<>();
		fights.forEach(f -> {
			fightersName.add(f.selectXpath("//div[@class='fighter left_side']/h3/a").text());
			
			fightersName.add(f.selectXpath("//div[@class='fighter right_side']/h3/a").text());
		});
		
		// pega o nome dos demais lutadores
		List<Element> fightersAnchors = getFightersAnchor(fights);
		fightersAnchors.forEach(f -> {
			fightersName.add(f.text());
		});
		
		return fightersName;
	}
	
	
	// pega as caracteristicas f??sicas do lutador
	private static List<List<String>> getFighterBio(List<Document> docs) {
		
		List<List<String>> fighterStatList = new ArrayList<>();
		docs.forEach(doc -> {
			// pega os elementos da bio do lutador
			List<Element> fighterData = doc.selectXpath("//div[@class='bio-holder']");
			List<Element> trElements = new ArrayList<>();
			fighterData.forEach(data -> {
				trElements.addAll(data.getElementsByTag("tr"));
			});
			
			// pega as strings e coloca numa lista composta
			List<String> fighterStat = new ArrayList<>();
			trElements.forEach(f -> {
				fighterStat.add(f.text());
				if (fighterStat.size() >= 3) {
					// faz a copia da lista fighterStat
					List<String> copyList = List.copyOf(fighterStat);
					// adiciona a copia na lista composta
					fighterStatList.add(copyList);
					// limpa a lista fighterStat
					fighterStat.clear();
				}
			});
				
			
		});
		
		return fighterStatList;
	}
	
	// retorna vitorias e derrotas de cada lutador
	private static List<List<String>> getCartel(List<Document> docs) {
		// pega o cartel
		List<Element> winLoseElements = new ArrayList<>();
		docs.forEach(doc -> {
			List<Element> divCartel = doc.selectXpath("//div[@class='winsloses-holder']");
			
			winLoseElements.addAll(divCartel.get(0).getElementsByClass("winloses win"));
			winLoseElements.addAll(divCartel.get(0).getElementsByClass("winloses lose"));
		});
		
		// pega as vitorias e derrotas de cada lutador e coloca em uma lista composta
		List<List<String>> winLoseLists = new ArrayList<>();
		List<String> winLose = new ArrayList<>();
		winLoseElements.forEach(wl -> {
			winLose.add(wl.text());
			if (winLose.size() >= 2) {
				// faz a copia da lista winLose
				List<String> copyList = List.copyOf(winLose);
				winLoseLists.add(copyList);
				winLose.clear();
			}
		});
		
		return winLoseLists;
	}
	
	private static List<String> getCountry(List<Document> docs) {
		// Pega o pa??s
		List<String> fighterCountry = new ArrayList<>();
		docs.forEach(doc -> {
			List<Element> divCountry = doc.getElementsByClass("fighter-nationality");
			fighterCountry.add(divCountry.get(0).text());
		});
		return fighterCountry;
	}
	
	// Pega o historico de lutas de cada lutador
	private static List<List<String>> getFightHistory(List<Document> docs) throws IOException {
		
		List<List<String>> fighterHistoryList = new ArrayList<>();
		for (Document docStat : docs) {		
				
			List<Element> divHistory = docStat.getElementsByClass("module fight_history");
			List<Element> trHistory = divHistory.get(0).getElementsByTag("tr");
			List<String> fighterHistory = new ArrayList<>();
			for (int c = 0; c < trHistory.size(); c++) {
				if (c != 0) {
					String linha = trHistory.get(c).getElementsByTag("td").text();
					fighterHistory.add(linha);
				}
			}
			// Adiciona na lista de historicos
			fighterHistoryList.add(fighterHistory);
		}
		return fighterHistoryList;
	}
	
	private static List<Document> getDocs(List<String> links) {
		List<Document> docs = new ArrayList<>();
		links.forEach(link -> {
			docs.add(connectJsoup(link));
		});
		
		return docs;	
	}

	public static List<Fighter> fetchFighters() {
		
		try {
			// Pega a url da primeira pagina dos eventos no sherdog
			// String url = getUrlString();
			Document doc = connectJsoup("https://www.sherdog.com/events/UFC-on-ESPN-42-Thompson-vs-Holland-94046");
			List<Element> fights = doc.select("div.inner-wrapper");
			
			List<String> fightersName = getFightersName(fights);
			List<String> fighterLinks = getFighterLink(fights);
			List<Document> docs = getDocs(fighterLinks);
			List<String> countrys = getCountry(docs);
			List<List<String>> cartel = getCartel(docs);
			List<List<String>> bio = getFighterBio(docs);
			List<List<String>> history = getFightHistory(docs);
			
			// instancia o objeto e adiciona na lista de objetos
			List<Fighter> fighters = new ArrayList<>();
			for (int c = 0; c < fightersName.size(); c++) {
				Fighter fighter = new Fighter();
				
				if (c % 2 == 0) {
					fighter.setCorner(Corner.red);
				}
				else {
					fighter.setCorner(Corner.blue);
				}
				
				fighter.setName(fightersName.get(c));
				fighter.setCountry(countrys.get(c));
				fighter.setAge(bio.get(c).get(0));
				fighter.setHeight(bio.get(c).get(1));
				fighter.setWeight(bio.get(c).get(2));
				fighter.setCartel(cartel.get(c).get(0) +" "+ cartel.get(c).get(1));
				for (int i = 0; i < history.get(c).size(); i++) {
					fighter.addFightHistory(history.get(c).get(i));
				}
				
				fighters.add(fighter);
			}
			return fighters;
	
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}

	public static void main(String[] args) throws IOException {

		List<Fighter> fighters = fetchFighters();
		
		fighters.forEach(f -> {
			System.out.println(f.getCorner());
			System.out.println(f.getName());
			System.out.println(f.getAge());
			System.out.println(f.getCountry());
			System.out.println(f.getHeight());
			System.out.println(f.getWeight());
			System.out.println(f.getCartel());
			System.out.println(f.getFightHistory());
			System.out.println();
			
		});
		
	}

}
