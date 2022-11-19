package com.marcelo.main.scrapping;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class UfcScrapping {

	public static List<String> fetchFighters() {

		WebClient webClient = new WebClient(BrowserVersion.CHROME);
		webClient.getOptions().setCssEnabled(false);
		webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
		webClient.getOptions().setThrowExceptionOnScriptError(false);
		webClient.getOptions().setPrintContentOnFailingStatusCode(false);

		// Pegar a url da primeira pagina do envento do ufc
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
			Document doc = Jsoup.connect("https://www.ufc.com.br/event/ufc-fight-night-november-19-2022#10192").get();

			List<Element> fightCard = doc.getElementsByClass("fight-card");

			List<String> titString = new ArrayList<>();

			fightCard.forEach(action -> {
				titString.add(action.getElementsByClass("c-event-fight-card-broadcaster__card-title").text());
			});
			System.out.println(titString);

			List<Element> fights = new ArrayList<>();

			fightCard.forEach(action -> {
				fights.addAll(action.getElementsByTag("li"));
			});

			// pega os nomes dos lutadores de cada corner
			List<String> rdFighters = new ArrayList<>();
			List<String> blFighters = new ArrayList<>();

			fights.forEach(action -> {
				String fighterRed = action
						.getElementsByClass("c-listing-fight__corner-name c-listing-fight__corner-name--red").text();
				String fighterBl = action
						.getElementsByClass("c-listing-fight__corner-name c-listing-fight__corner-name--blue").text();
				if (fighterRed != "" && fighterBl != "") {
					rdFighters.add(fighterRed);
					blFighters.add(fighterBl);
				}

			});
			
			
			
			List<String> redFighterStat = new ArrayList<>();
			List<String> blueFighterStat = new ArrayList<>();
			List<Element> statElements = new ArrayList<>();
			
			fights.forEach(action -> {
				statElements.addAll(action.getElementsByClass("details-content__iframe-wrapper"));
			});
			
			// estatisticas
			fights.forEach(action -> {
				redFighterStat.add(action.getElementsByClass("c-stat-compare__group-1 red").text());
				blueFighterStat.add(action.getElementsByClass("c-stat-compare__group-2 blue").text());
			});
			
			System.out.println(redFighterStat);
			System.out.println(blueFighterStat);
			
			System.out.println(fights);

			// junta todos os nomes
			List<String> fighters = new ArrayList<>();
			for (int f = 0; f < rdFighters.size(); f++) {
				fighters.add(rdFighters.get(f));
				fighters.add(blFighters.get(f));
			}

			// retorna todos os lutadores
			return fighters;

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
