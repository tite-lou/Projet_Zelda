package progWeb.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Map;

import org.json.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import model.Character;
import model.Universe;

@Controller
public class MyController {
	
	@RequestMapping(value = "/chooseCharacter", method = RequestMethod.GET)
	public void chooseCharacter(HttpServletRequest request, HttpServletResponse response)
			throws UnsupportedEncodingException, IOException {
		response.sendRedirect("/chooseCharacter.html");
	}

	/**
	 * ListCharacters retourne la liste de  personnage au JS afin de l'afficher à l'utilisateur pour la selection
	 * @return list<Character> 
	 */
	@RequestMapping(value="/listCharacters",method=RequestMethod.POST)
    public @ResponseBody String listCharacters() {
		ArrayList<Character> Characters = Universe.getCharacters();
		JSONArray lesCh= new JSONArray();
		
		for(Character c : Characters) {
			JSONObject character = new JSONObject();
			character.put("name", c.getName());
			character.put("pv", c.getHpMax());
			character.put("attack", c.getAttack());
			character.put("esquive", c.getDodgeProbability());
			lesCh.put(character);
		}
		System.out.println("Les personnage:");
		System.out.println(lesCh);
        return lesCh.toString();
    }
	
	@RequestMapping(value = "/param", method = RequestMethod.GET)
	public void showParamFromGet(HttpServletRequest request, HttpServletResponse response)
			throws UnsupportedEncodingException, IOException {
		Map<String, String[]> param = request.getParameterMap();
		for (Object key : param.keySet()) {
			System.out.println("" + key + " : " + param.get(key)[0]);
		}
	}
	
	@RequestMapping(value = "/param", method = RequestMethod.POST)
	public void showParamFromPost(HttpServletRequest request, HttpServletResponse response)
			throws UnsupportedEncodingException, IOException {
		Map<String, String[]> param = request.getParameterMap();
		for (Object key : param.keySet()) {
			System.out.println("" + key + " : " + param.get(key)[0]);
		}
	}

	/**
	 * charSelected est une méthode permetant de conserver le personnage selectionné à l'aide de cookie.
	 * Le cookie servira à effectuer le combat
	 * @param request
	 * @param response
	 * @param name
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 */
	@RequestMapping(value = "/charSelected", method = RequestMethod.POST)
	public void choiceCharacter(HttpServletRequest request, HttpServletResponse response,  @RequestParam(name="name") String name) 
			throws UnsupportedEncodingException, IOException {
		ArrayList<Character> Characters = Universe.getCharacters();
		for(Character c : Characters) {
			if(c.getName().equals(name)){
				System.out.println("Sélection du personnage :");
				System.out.println(c.toString());
				Cookie ch_attack = new Cookie("ch_attack", Integer.toString(c.getAttack()));
				Cookie h_name = new Cookie("h_name", c.getName());
				Cookie ch_hp_max = new Cookie("ch_hp_max", Integer.toString(c.getHpMax()));
				Cookie ch_hp = new Cookie("ch_hp", Integer.toString(c.getHpMax()));
				Cookie ch_dodge = new Cookie("ch_dodge", Double.toString(c.getDodgeProbability()));
				response.addCookie(h_name);
				response.addCookie(ch_hp_max);
				response.addCookie(ch_hp);
				response.addCookie(ch_attack);
				response.addCookie(ch_dodge);
			}
		}
		response.sendRedirect("/combat.html");
	}
	
	/**
	 * Méthode retounant au JS les informations sur le personnages selectionnée (js permet un affichage actif)
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 */
	@RequestMapping(value="/getCharacter",method=RequestMethod.POST)
    public @ResponseBody String Character(HttpServletRequest request, HttpServletResponse response) 
    		throws UnsupportedEncodingException, IOException {
		
		String h_name = "", ch_hp = "", ch_hp_max = "", ch_dodge = "", ch_attack = "";
		Cookie[] Cookies = request.getCookies();
		
		if(Cookies == null) {
			response.sendRedirect("/noCharacter");
		} else {
			for(Cookie c : Cookies) {
				switch(c.getName()) {
				  case "h_name":
					h_name = c.getValue();
				    break;
				  case "ch_hp":
					ch_hp = c.getValue();
				    break;
				  case "ch_hp_max":
					ch_hp_max = c.getValue();
					break;
				  case "ch_dodge":
					ch_dodge = c.getValue();
					break;
				  case "ch_attack":
					ch_attack = c.getValue();
					break;
				  default:
				}
			}
		}

		JSONArray CharStat = new JSONArray();
		JSONObject character = new JSONObject();
		
		character.put("h_name", h_name);
		character.put("ch_hp", ch_hp);
		character.put("ch_hp_max", ch_hp_max);
		character.put("ch_attack", ch_attack);
		character.put("ch_dodge", ch_dodge);
		CharStat.put(character);
		
		System.out.println("Test cooki du perso :"+CharStat);
        return CharStat.toString();
    }
	
	/**
	 * Méthode retournant au JS du combat le monstre actuel du combat. 
	 * Les monstres suivent une liste et une fois tuer via previous Foe on actualise le monstre au suivant de la liste
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 */
	@RequestMapping(value="/getMonster",method=RequestMethod.POST)
    public @ResponseBody String Monster(HttpServletRequest request, HttpServletResponse response) 
    		throws UnsupportedEncodingException, IOException {
		int previousFoe = -1;
		for (Cookie c : request.getCookies()) {
			if (c.getName().equals("foeNumber")) {
				previousFoe = Integer.parseInt(c.getValue());
			}
		}
		try {
			Character foe = Universe.getMonsters().get(previousFoe + 1);
			response.addCookie(new Cookie("foeNumber", "" + (previousFoe + 1)));
			Cookie m_attack = new Cookie("m_attack", Integer.toString(foe.getAttack()));
			Cookie m_name = new Cookie("m_name", foe.getName());
			Cookie m_hp_max = new Cookie("m_hp_max", Integer.toString(foe.getHpMax()));
			Cookie m_hp = new Cookie("m_hp", Integer.toString(foe.getHpMax()));
			Cookie m_dodge = new Cookie("m_dodge", Double.toString(foe.getDodgeProbability()));
			
			response.addCookie(m_name);
			response.addCookie(m_hp_max);
			response.addCookie(m_hp);
			response.addCookie(m_attack);
			response.addCookie(m_dodge);
			
			JSONArray monsterStat = new JSONArray();
			JSONObject monster = new JSONObject();
			
			monster.put("m_name", m_name.getValue());
			monster.put("m_hp", m_hp.getValue());
			monster.put("m_hp_max", m_hp_max.getValue());
			monster.put("m_attack", m_attack.getValue());
			monster.put("m_dodge", m_dodge.getValue());
			monsterStat.put(monster);
			
			
			System.out.println("Apercu cookie monstre: "+monsterStat);
			
			return monsterStat.toString();

		} catch (IndexOutOfBoundsException e) {
			response.addCookie(new Cookie("foeNumber", "" + (previousFoe + 1)));
			System.out.println("Victoire");
			response.sendRedirect("/fin.html");
			return e.getMessage();
		}
	}
	
	/*@RequestMapping(value = "/character", method = RequestMethod.GET)
	public void showCharacter(HttpServletRequest request, HttpServletResponse response)
			throws UnsupportedEncodingException, IOException {
		String h_name = "", ch_hp = "", ch_attack = "";
		Cookie[] Cookies = request.getCookies();
		if(Cookies == null) {
			response.sendRedirect("/noCharacter");
		} else {
			for(Cookie c : Cookies) {
				switch(c.getName()) {
				  case "name":
					h_name = c.getValue();
				    break;
				  case "hp":
					ch_hp = c.getValue();
				    break;
				  case "attack":
					ch_attack = c.getValue();
					break;
				  default:
				    
				}
			}
		}
		//Affichage comme toString()
		String streamContent = "<!DOCTYPE html><html><head><meta charset=\"UTF-8\"></head><body><p>"+h_name+": hpMax="+ch_hp+", attack="+ch_attack+"</p></body></html>";
		
		//Affichage normal
		//String streamContent = "<!DOCTYPE html>	<html>	<head>	<meta charset=\"UTF-8\"><title>Character "+h_name+"</title></head><body><p>Personnage "+h_name+"<br></p><p>HP : "+ch_hp+"</p><p>Attack : "+ch_attack+"</p></body></html>";
		response.getOutputStream().write(streamContent.getBytes("UTF-8"));
	}*/
	
	
	
	/**
	 * Méthode permettant de faire subir les dégats à chaque parti et ainsi changer de monstre ou déclarer 
	 * la victoire ou la fin du jeu 
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 */
	@RequestMapping(value = "/takeDamage", method = RequestMethod.POST)
	public @ResponseBody String takeDamage(HttpServletRequest request, HttpServletResponse response)
			throws UnsupportedEncodingException, IOException {
		Cookie[] cookies = request.getCookies();
		String h_name, m_name;
		int ch_hp=0,m_hp=0;
		int ch_attack=0, m_attack=0;
		double ch_dodge =0, m_dodge=0;
		
		 if(cookies != null) {
			 for(Cookie cookie : cookies) {
	                switch(cookie.getName()) {
	                    case "h_name":
	                        h_name = cookie.getValue();
	                        break;
	                    case "ch_hp":
	                        ch_hp = Integer.parseInt(cookie.getValue());
	                        break;
	                    case "ch_attack":
	                        ch_attack = Integer.parseInt(cookie.getValue());
	                        break;
	                    case "ch_dodge":
	                        ch_dodge = Double.parseDouble(cookie.getValue());
	                        break;
	                    case "m_name":
	                        m_name = cookie.getValue();
	                        break;
	                    case "m_hp":
	                        m_hp = Integer.parseInt(cookie.getValue());
	                        break;
	                    case "m_attack":
	                        m_attack = Integer.parseInt(cookie.getValue());
	                        break;
	                    case "m_dodge":
	                        m_dodge = Double.parseDouble(cookie.getValue());
	                        break;
	                }
	            }
		 }
		
		double dodge_number = 0;
		
		 if(cookies != null) {
	            for (Cookie cookie : cookies) {
	              if (cookie != null) {
	                switch(cookie.getName()) {
	                	case "ch_hp":
	                			dodge_number = Math.random();
	                			if(dodge_number <= ch_dodge) {
	                				System.out.println("Esquive");
	                				break;
	                		}else {
	                			int vieHero = ch_hp-m_attack;
	                			if(vieHero<0) {
									ch_hp = 0;
								}else {
									ch_hp = vieHero; 
								}
	                			cookie.setValue(Integer.toString(ch_hp));
	                			response.addCookie(cookie);
	                			System.out.println("Vie Hero actuel :"+ch_hp);
	                			System.out.println("Vie Hero actuel :"+vieHero);
	                		}
	                		break;
	                	case "m_hp":
	                		dodge_number= Math.random();
	                		if(dodge_number <= m_dodge) {
	                			break;
	  
	                		}else {
	                			int vieEnnemi = (int)Math.round(m_hp-ch_attack);
	                			if(vieEnnemi<0) {
	                				m_hp = 0;
								}else {
									m_hp = vieEnnemi;
								}
	                			cookie.setValue(Integer.toString(m_hp));
	                			response.addCookie(cookie);
	                			System.out.println("Vie Ennemi actuel :"+vieEnnemi);
	                		}
	                		break;
	                	default : 
	                		continue;
	                	}
	                   
	                }
	            }
	        }
		 JSONArray newStat = new JSONArray();
		 JSONObject stat = new JSONObject();
		 
		 stat.put("ch_hp", Integer.toString(ch_hp));
		 stat.put("m_hp", Integer.toString(m_hp));
		 newStat.put(stat);
	
		 return newStat.toString();
	}

	
	@RequestMapping(value = "/nextFoe", method = RequestMethod.GET)
	public void nextFoe(HttpServletRequest request, HttpServletResponse response)
			throws UnsupportedEncodingException, IOException {
		int previousFoe = -1;
		for (Cookie c : request.getCookies()) {
			if (c.getName().equals("foeNumber")) {
				previousFoe = Integer.parseInt(c.getValue());
			}
		}
		try {
			Character foe = Universe.getMonsters().get(previousFoe + 1);
			response.addCookie(new Cookie("foeNumber", "" + (previousFoe + 1)));
			response.addCookie(new Cookie("foeName", foe.getName()));
			response.addCookie(new Cookie("foeHP", " " + foe.getHpMax()));
			response.addCookie(new Cookie("foeAttack", "" + foe.getAttack()));
		} catch (IndexOutOfBoundsException e) {
			response.getOutputStream().write("Tous les ennemis sont vaincus".getBytes("UTF-8"));
		}
	}
	
	/**
	 * Méthode qui permet de relancer le jeu sur le choix du personnage et remet à jour les cookie
	 * @param request
	 * @param response
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 */
	@RequestMapping(value="/restart",method=RequestMethod.GET)
    public void restart(HttpServletRequest request, HttpServletResponse response) 
    		throws UnsupportedEncodingException, IOException {
			for (Cookie cookie : request.getCookies()) {
				if (cookie.getName().equals("foeNumber")) {
					cookie.setMaxAge(0);
					response.addCookie(cookie);
				}
			}
			response.sendRedirect("/chooseCharacter");
	}
	
	/**
	 * Méthode permettant de récupérer les derniéer info du cooki afin d'afficher le nombre de victoire sur 
	 * chaque monstre du personnage ainsi que le nom du personnage et du dernier monste encore en liste
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 */
	@RequestMapping(value="/endStat",method=RequestMethod.POST)
    public @ResponseBody String endStat(HttpServletRequest request, HttpServletResponse response) 
    		throws UnsupportedEncodingException, IOException {
		String h_name = "", m_name = "", foeNumber = "";
		Cookie[] Cookies = request.getCookies();
		
		if(Cookies == null) {
			
		} else {
			for(Cookie cookie : Cookies) {
				switch(cookie.getName()) {
				  case "h_name":
					h_name = cookie.getValue();
				    break;
				  case "foeNumber":
					foeNumber = cookie.getValue();
				    break;
				  case "m_name":
					m_name = cookie.getValue();
				  default:
				}
			}
		}

		JSONArray endStat = new JSONArray();
		JSONObject info = new JSONObject();
		
		info.put("h_name", h_name);
		info.put("m_name", m_name);
		info.put("foeNumber", foeNumber);
		endStat.put(info);
		
		System.out.println("Stat de fin du jeu:");
		System.out.println(endStat);
        return endStat.toString();
    }

}
