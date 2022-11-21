package com.marcelo.main.scrapping;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebWindowListener;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;

public class UfcScrapping2 {

	public static List<String> fetchFighters() {

		WebClient webClient = new WebClient(BrowserVersion.CHROME);
		webClient.getOptions().setCssEnabled(false);
		webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
		webClient.getOptions().setThrowExceptionOnScriptError(false);
		webClient.getOptions().setPrintContentOnFailingStatusCode(false);

		// Pegar a url da primeira pagina dos eventos no sherdog
		try {
			/*
			HtmlPage page = webClient.getPage("https://www.ufc.com.br/events");

			// Pegando o link da pagina da luta
			List<?> btnAnchor = page.getByXPath(
					"//*[@id=\"events-list-upcoming\"]/div/div/div[2]/div/div/section/ul/li[1]/article/div[3]/h3/a");
			HtmlAnchor link = (HtmlAnchor) btnAnchor.get(0);
			String url = "https://www.ufc.com.br" + link.getHrefAttribute();
			
			webClient.close();
			*/

			// conecta na url obtida
			Document doc = Jsoup.connect("https://www.sherdog.com/events/UFC-Fight-Night-215-Lewis-vs-Spivak-94045").get();
			
			List<Element> fights = doc.select("div.inner-wrapper");
			
			List<Element> redFightersAnchors = new ArrayList<>();
			List<Element> blueFightersAnchors = new ArrayList<>();
			List<String> redFightersLink = new ArrayList<>();
			List<String> blueFightersLink = new ArrayList<>();
			List<String> blueFightersName = new ArrayList<>();
			List<String> redFightersName = new ArrayList<>();
			
			// pega o nome e os links dos lutadores da luta principal
			fights.forEach(f -> {
				redFightersName.add(f.selectXpath("//div[@class='fighter left_side']/h3/a").text());
				redFightersLink.add("https://www.sherdog.com" + f.selectXpath("//div[@class='fighter left_side']/h3/a").first().attr("href"));
				
				blueFightersName.add(f.selectXpath("//div[@class='fighter right_side']/h3/a").text());
				blueFightersLink.add("https://www.sherdog.com" + f.selectXpath("//div[@class='fighter right_side']/h3/a").first().attr("href"));
			});
			
			// pega as anchors de todos os lutadores
			fights.forEach(f -> {
				redFightersAnchors.addAll(f.selectXpath("//div[@class='fighter_list left']/div/a"));
				blueFightersAnchors.addAll(f.selectXpath("//div[@class='fighter_list right']/div/a"));			
			});
			
			redFightersAnchors.forEach(f -> {
				redFightersName.add(f.text());
				redFightersLink.add("https://www.sherdog.com" + f.attr("href"));
			});
			
			blueFightersAnchors.forEach(f -> {
				blueFightersName.add(f.text());
				blueFightersLink.add("https://www.sherdog.com" + f.attr("href"));
			});
			
			
			List<Element> trElements = new ArrayList<>();
			List<List<String>> fighterStatList = new ArrayList<>();
			List<Element> winLoseElements = new ArrayList<>();
			List<String> fighterCountry = new ArrayList<>();
			 
			List<List<String>> fighterHistoryList = new ArrayList<>();
			
			for (String s : redFightersLink) {		
				try {
					Document docStat = Jsoup.connect(s).get();
					
					// pega a bio do lutador	
					List<Element> fighterData = docStat.selectXpath("//div[@class='bio-holder']");	
					for (Element e : fighterData) {
						trElements.addAll(e.getElementsByTag("tr"));
					}
					
					// pega o cartel
					List<Element> divCartel = docStat.selectXpath("//div[@class='winsloses-holder']");
					winLoseElements.addAll(divCartel.get(0).getElementsByClass("winloses win"));
					winLoseElements.addAll(divCartel.get(0).getElementsByClass("winloses lose"));
					
					// pega o país
					List<Element> divCountry = docStat.getElementsByClass("fighter-nationality");
					fighterCountry.add(divCountry.get(0).text());
					
					// pega o historico de lutas de cada lutador
					List<Element> divHistory = docStat.getElementsByClass("module fight_history");
					List<Element> trHistory = divHistory.get(0).getElementsByTag("tr");
					List<String> fighterHistory = new ArrayList<>();
					for (int c = 0; c < trHistory.size(); c++) {
						if (c != 0) {
							String linha = trHistory.get(c).getElementsByTag("td").text();
							fighterHistory.add(linha);
						}
					}
					// adiciona na lista de historicos
					fighterHistoryList.add(fighterHistory);
					
				} catch (IOException e) {
					e.printStackTrace();
				}
			
			}
			
			// pega as caracteristicas físicas do lutador
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
			
			// pega as vitorias e derrotas de cada lutador
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
			
			// ver como dividir a lista de hostorico de cada lutador?
			
			
			
			
			//System.out.println(fighterStatList.get(0).get(2));
			
			/*
			fighterStatList.forEach(action -> {
				action.forEach(e -> {
					System.out.println(e);
					});
			});
			*/
			
	
		} catch (Exception e) {
			System.out.println(e);
		}
		return null;
	}

	public static void main(String[] args) throws IOException {

		List<String> fighters = fetchFighters();
		/*
		for (int f = 0; f < fighters.size(); f++) {
			if (f % 2 == 0) {
				System.out.println(fighters.get(f));

				System.out.println("VS");
			} else {
				System.out.println(fighters.get(f));
				System.out.println();
			}

		}
		*/

	}

}
