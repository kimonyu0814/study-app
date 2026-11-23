/* 영어 지문 정리 · 암기 데이터
   출처: 2026 고1 10월 모의고사(수업용자료 B) · EBS 올림포스 영어독해 기본 02 (Unit 15~17)
   q: 유도 질문 — keys 는 '꼭 들어가야 할 말' (각 칸은 같은 뜻의 여러 표현 중 하나만 들어가면 됨)
   key: 외울 핵심 문장 — [ ] 안이 빈칸 */
const ENG = [
{ id:"m23", set:"10월 모의고사", no:"23번", type:"주제", title:"내부 고발자 보호의 필요성",
  q:[
    { ask:"이 글은 무엇(누구)에 관한 글인가요?", keys:[["내부 고발자","내부고발자","제보자","whistleblower"],["보호"]],
      model:"내부 고발자(whistleblower) 보호" },
    { ask:"내부 고발자가 신고를 못 하면 어떤 일이 생기나요?", keys:[["적발","처벌","드러나"],["부정적","피해","해"]],
      model:"보복이 두려워 신고를 못 하면 위법 행위가 오래 적발·처벌되지 않고, 회사·제삼자·경제·대중·법치주의까지 피해를 입는다." },
    { ask:"내부 고발자 보호의 두 가지 기능은?", keys:[["개인","행위"],["공익"]],
      model:"① 그 개인의 행위를 보호하는 것 ② 공익을 보호하는 것" },
    { ask:"글 전체의 요지를 한 문장으로 정리해 보세요.", keys:[["내부 고발자","내부고발자","제보자"],["보호"],["공익"]],
      model:"균형 잡힌 내부 고발자 보호는 고발자 개인의 행위와 공익을 모두 지켜 주므로 필요하다." }
  ],
  flow:["기업 위법 행위 증가 → 용감한 제보자 덕분에 드러남","그래서 내부 고발자 보호가 주목받음","신고 못 하면 → 회사·경제·대중·법치까지 피해","보호의 두 기능: 개인 행위 + 공익","결론: 균형 잡힌 보호 = 둘 다에 긍정적"],
  answer:"⑤ need of whistleblower protection to guard their actions and the public good",
  key:["Protecting those who would assist in reducing [unlawful] or even criminal behaviour has two main [functions]: protecting the individual's actions as well as protecting the [public good]."],
  vocab:[["illegal action","위법 행위"],["in particular","특히"],["corporate","기업의, 회사의"],["come to light","세상에 드러나다"],["misconduct","위법 행위"],["expose","드러내다, 폭로하다"],["informant","제보자"],["come into focus","주목받게 되다"],["violation","위반"],["undetected","적발되지 않은"],["unpunished","처벌받지 않는"],["reprisal","보복"],["third party","제삼자"],["the rule of law","법치주의"],["unlawful","위법한, 불법의"],["public good","공익"]] },

{ id:"m24", set:"10월 모의고사", no:"24번", type:"제목", title:"미래에 대한 환상은 행동을 막는다",
  q:[
    { ask:"우리는 목표에 대해 무엇과 '사랑에 빠지나요'?", keys:[["상상","환상","생각","idea"]],
      model:"목표 그 자체가 아니라 목표에 대한 '상상(idea)'과 사랑에 빠진다." },
    { ask:"긍정적인 환상이 왜 노력을 줄이게 하나요?", keys:[["성취감"],["거짓","때 이른","가짜"]],
      model:"환상이 거짓되고 때 이른 성취감을 주기 때문에 실제 노력을 덜 하게 된다." },
    { ask:"목표를 낭만화할 때 우리가 대충 넘기는(얼버무리는) 것은?", keys:[["복잡","어려움","현실"]],
      model:"새로운 행동을 삶에 통합하는 것의 복잡함과 어려움, 즉 일상의 현실" },
    { ask:"글의 요지를 한 문장으로 정리해 보세요.", keys:[["환상","상상"],["행동","노력"]],
      model:"미래에 대한 긍정적 환상은 머릿속으로 미리 성취를 즐기게 해서 실제 행동할 필요를 느끼지 못하게 만든다." }
  ],
  flow:["목표에 대한 상상은 일상의 현실과 동떨어짐","상상과 사랑에 빠져 하루하루의 변화를 생각 못 함","연구: 긍정적 환상 → 거짓된 성취감 → 노력 감소","목표를 낭만화하며 어려움을 얼버무림","환상은 지금 여기서 미래를 즐기게 해 행동 필요성을 없앰"],
  answer:"④ Fantasizing About the Future: A Barrier to Action",
  key:["Positive fantasies about our future can lead us to put [less] effort into our goals because our fantasies provide us with a [false] and [premature] sense of accomplishment."],
  vocab:[["disconnected","동떨어진"],["daily reality","일상적인 현실"],["push ~ out of mind","~을 생각에서 밀어내다"],["fantasy","환상"],["premature","때 이른"],["accomplishment","성취"],["romanticize","낭만화하다"],["gloss over","얼버무리다"],["intricacy","복잡함"],["sum up","요약하다"],["fantasize about","~에 대해 환상을 품다"],["reflect on","~에 대해 돌아보다"],["induce","유발하다"]] },

{ id:"m29", set:"10월 모의고사", no:"29번", type:"어법", title:"히드라·해파리 노화 연구의 한계",
  q:[
    { ask:"히드라와 불멸의 해파리는 어떤 특징이 있나요?", keys:[["노화","늙"],["죽지 않","불멸","가능성"]],
      model:"명백한 노화 징후가 없고, 나이가 들어도 죽을 가능성이 높아지지 않아 생물학적으로 불멸로 여겨진다." },
    { ask:"노화학자들은 왜 이 생물들에 흥분하나요?", keys:[["노화"],["단서"]],
      model:"노화 작용을 물리치는 방법에 대한 단서를 줄 수 있기 때문이다." },
    { ask:"필자가 경계하는 점(However 이후)은?", keys:[["인간"],["적용","관련","불분명","어렵"]],
      model:"그 발견이 인간의 노화에 얼마나 관련(적용)될지는 분명하지 않다. 쥐·생쥐 결과도 인간에 적용하기 어렵다." },
    { ask:"글의 요지를 한 문장으로 정리해 보세요.", keys:[["인간"],["적용","유용","시간"]],
      model:"히드라·해파리의 노화 연구는 흥미롭지만 인간에게 유용해지기까지는 매우 오랜 시간이 걸릴 수 있다." }
  ],
  flow:["히드라·해파리 = 노화 징후 없음 → 생물학적으로 불멸","노화 단서가 될 수 있어 노화학자들이 주목","하지만 인간 노화와의 관련성은 불분명","생물학이 보편적인 경우도 있지만 쥐 결과조차 인간 적용이 어려움","→ 인간에게 유용해지려면 오래 걸릴 것"],
  key:["It is not clear how [relevant] those findings will be to human [aging]."],
  vocab:[["immortal","불멸의"],["exhibit","보이다, 나타내다"],["obvious","명백한"],["likelihood","가능성"],["gerontologist","노화학자"],["clue","단서"],["defeat","물리치다"],["regenerate","재생하다"],["organism","생물체, 유기체"],["apparent","명백한, 분명한"],["relevant","관련 있는"],["finding","발견, 연구 결과"],["mammal","포유류"],["translate into","~에 적용하다"]] },

{ id:"m30", set:"10월 모의고사", no:"30번", type:"어휘", title:"발전소 보조금의 문제점",
  q:[
    { ask:"발전소들은 보조금·세금·규제 중 무엇을 선호하고, 왜 그런가요?", keys:[["보조금"],["정부","직접","비용"]],
      model:"보조금. 비용을 직접 내기보다 정부로부터 돈을 받는 것을 선호하기 때문이다." },
    { ask:"보조금으로 발전소의 기회(자유)가 늘어나면 누가 손해를 보나요?", keys:[["일반","개인","다른 사람"],["세금"]],
      model:"보조금을 대기 위해 늘어난 세금을 내야 하는 일반 개인들(다른 사람들의 기회 집합이 축소됨)" },
    { ask:"효율성 관점에서 보조금은 어떤 문제를 낳나요?", keys:[["가격","왜곡"],["소비","온난화"]],
      model:"회사가 자기 행위의 전체 비용을 부담하지 않아 시장을 왜곡한다 → 전기 가격은 낮아지고 소비는 늘어 지구 온난화가 악화될 수 있다." },
    { ask:"글의 요지를 한 문장으로 정리해 보세요.", keys:[["보조금"],["세금","소비","온난화","왜곡"]],
      model:"오염을 줄이기 위한 보조금은 일반 개인에게 세금 부담을 지우고 시장을 왜곡해 오히려 지구 온난화를 악화시킬 수 있다." }
  ],
  flow:["보조금·세금·규제 모두 석탄 전환 → 오염 감소 가능","발전소는 보조금을 선호(정부 돈이 좋으니까)","발전소의 기회 확대 = 일반 개인의 기회 축소(세금)","효율성: 전체 비용을 안 짐 → 왜곡","전기값↓ 소비↑ → 온난화 악화"],
  key:["The \"expansion\" of their opportunity set through subsidy has to be matched by the [contraction] of the opportunity set of others."],
  vocab:[["subsidy","보조금"],["regulation","규제"],["power plant","발전소"],["pollute","오염시키다"],["coal","석탄"],["transition","전환"],["take ~ into account","~을 고려하다"],["prefer A over B","B보다 A를 선호하다"],["expansion","확대"],["at the expense of","~을 희생시키면서"],["taxation","과세"],["efficiency","효율성"],["distort","왜곡하다"],["emission","배출(량)"],["end up","결국 ~하게 되다"],["consumption","소비"],["worsen","악화시키다"]] },

{ id:"m31", set:"10월 모의고사", no:"31번", type:"빈칸", title:"Star Trek 세계와 희소성 없는 경제",
  q:[
    { ask:"고전 경제학은 어떤 개념에 기초하나요?", keys:[["희소","충분하지 않","부족"]],
      model:"모든 사람에게 원하는 것을 다 주기에는 어떤 것이든 충분하지 않다는 개념(희소성)" },
    { ask:"그래서 사람들은 무엇을 이용하나요?", keys:[["시장"],["가격"]],
      model:"재화·서비스·자원을 가장 큰 이익이 나는 곳에 배분하려고 시장과 가격 책정을 이용한다." },
    { ask:"Star Trek 세계는 고전 경제학의 세계와 무엇이 다른가요?", keys:[["희소","풍요","부족"]],
      model:"기술 덕분에 무엇이든 복제·이동할 수 있어 희소성이 거의 없는 풍요로운 세계이다." },
    { ask:"이 글이 현재와 연결 짓는 것은?", keys:[["생성형","AI","인공지능"],["풍요"]],
      model:"생성형 AI가 우리 사회의 많은 측면을 마찬가지로 매우 풍요롭게 만들 잠재력이 있다." }
  ],
  flow:["Star Trek을 경제학 렌즈로 보면 곧 닥칠 세계가 보임","고전 경제학 = 희소성 → 시장·가격으로 배분","Star Trek 세계 = 희소성 거의 없음(기술로 복제·이동)","모두 교육받아 풍요에 참여 → 모두가 탐험가·연구원·예술가","생성형 AI도 사회를 풍요롭게 만들 잠재력"],
  key:["All of classical economics is based on the notion that there is rarely [enough] of anything to give everyone everything they want or need."],
  vocab:[["universe","세계, 우주"],["lens","관점, 렌즈"],["be upon","~에게 닥쳐 있다"],["notion","개념"],["pricing","가격 책정"],["allocate","배분하다"],["result in","~을 낳다"],["scarcity","희소성"],["replicate","복제하다"],["humanity","인류"],["prosperity","번영, 풍요"],["generative","생성형의"],["potential","잠재력"],["abundant","풍요로운"]] },

{ id:"m33", set:"10월 모의고사", no:"33번", type:"빈칸", title:"공간은 경험의 매개체",
  q:[
    { ask:"경험과 공간은 어떤 관계인가요?", keys:[["분리","떼어","뗄 수 없"]],
      model:"모든 경험은 물리적 공간에서 일어나므로 공간은 그 안에서 일어난 사건과 분리될 수 없다." },
    { ask:"흔히 생각하는 공간의 역할과 필자가 말하는 역할을 비교해 보세요.", keys:[["배경","외부"],["매개"]],
      model:"흔히: 경험 바깥의 단순한 배경·상황 / 필자: 모든 행동·생각·기억·감정의 매개체" },
    { ask:"Poulet(Proust 인용)는 장소를 무엇에 비유했나요?", keys:[["옷","의복"]],
      model:"사람을 감추는 동시에 특징짓는 의복. 장소가 없으면 존재는 추상적 개념에 불과하다." },
    { ask:"글의 요지를 한 문장으로 정리해 보세요.", keys:[["공간","장소"],["매개","배경"]],
      model:"공간(장소)은 경험의 수동적 배경이 아니라 모든 경험의 매개체이다." }
  ],
  flow:["경험은 공간에서 일어남 → 공간과 사건은 분리 불가","장소·공간이 경험의 핵심이라면","공간 틀 = 모든 경험의 매개체(단순 배경 ×)","Poulet: 장소는 존재를 감싸는 옷","장소 없으면 존재는 추상"],
  answer:"⑤ challenges the idea of place as passive backdrop",
  key:["The spatial framework is the [medium] of all experiences, not merely a [background] or context exterior to experience."],
  vocab:[["take place","일어나다"],["inseparable","분리할 수 없는"],["intertwine","뒤얽히다"],["spatial","공간의"],["framework","틀"],["medium","매개체"],["exterior","외부의"],["commonly supposed","흔히 생각되는"],["elegantly","유려하게"],["garment","의복"],["disguise","변장"],["abstraction","추상적 개념"],["backdrop","배경"]] },

{ id:"m34", set:"10월 모의고사", no:"34번", type:"빈칸", title:"모델의 단순화 가정",
  q:[
    { ask:"모든 모델이 반드시 해야 하는 것은?", keys:[["단순화"],["가정"]],
      model:"어떤 것들은 그냥 주어진 것으로 받아들이는 '단순화하는 가정'을 세워야 한다." },
    { ask:"Wallace와 Darwin의 예는 무엇을 보여 주나요?", keys:[["변이","유전"],["주어진","몰랐","알지 못"]],
      model:"변이 생성과 유전이 어떻게 작동하는지 거의 몰랐지만 그것을 주어진 것으로 받아들이고도 진화 모델을 제시했다." },
    { ask:"물리학자의 예는 무엇인가요?", keys:[["질량","전하"],["측정","받아들"]],
      model:"전자가 왜 그 질량·전하를 갖는지 설명하지 않고 측정값을 받아들이고 넘어간다." },
    { ask:"글의 요지(빈칸 내용)를 정리해 보세요.", keys:[["다른 수준","모든 수준","완전"],["유용"]],
      model:"다른 모든 수준에서 일어나는 일을 완전히 설명하지 않고도 한 분석 수준에서 유용한 모델을 만들 수 있다." }
  ],
  flow:["어떤 모델이든 주어진 것으로 받아들일 것이 있음","모든 모델은 단순화 가정이 필요","Wallace·Darwin: 유전 원리 몰라도 진화 모델 제시","물리학자: 입자의 질량·전하를 측정값 그대로 수용","핵심: 다른 수준을 다 몰라도 한 수준에서 유용한 모델 가능"],
  answer:"⑤ without a complete accounting of what's happening at all other levels",
  key:["All models must make [simplifying] [assumptions] of this nature."],
  vocab:[["delineate","정하다, 묘사하다"],["take ~ as a given","~을 주어진 것으로 받아들이다"],["emerge","출현하다"],["simplifying assumption","단순화하는 가정"],["verbal","언어적인"],["variation","변이"],["inheritance","유전"],["mass","질량"],["charge","전하"],["fundamental particle","기본 입자"],["atomic bond","원자 결합"],["measured value","측정값"]] },

{ id:"m38", set:"10월 모의고사", no:"38번", type:"문장 삽입", title:"진실 착각 효과",
  q:[
    { ask:"1977년 실험에서 학생들은 무엇을 했나요?", keys:[["60"],["참","거짓","확신"]],
      model:"60개의 일반 상식 진술문이 참인지 거짓인지 얼마나 확신하는지 2주 간격으로 세 번 평가했다." },
    { ask:"실험의 결정적인 조건은?", keys:[["20"],["반복"]],
      model:"60개 중 20개만 매번 반복되고 나머지 40개는 새로운 것이었다." },
    { ask:"놀라운 결과는 무엇이었나요?", keys:[["반복"],["참","진실"]],
      model:"진술이 참이든 거짓이든 반복될수록 진실이라고 믿는 정도가 올라갔다." },
    { ask:"이 현상의 이름과 뜻을 정리해 보세요.", keys:[["진실 착각","illusory truth"],["자주","반복"]],
      model:"진실 착각 효과(illusory truth effect): 어떤 말을 자주 들을수록 더 사실처럼 들린다." }
  ],
  flow:["1977년 심리학자들의 실험","60개 상식 진술 → 참/거짓 확신도 평가 ×3회(2주 간격)","20개만 반복, 40개는 새것","반복된 진술의 진실성 평가가 올라감(참·거짓 무관)","= 진실 착각 효과"],
  key:["Regardless of whether the statement was true or false, belief in the truth of the claim went up as a function of [repetition].","This became known as the [illusory] truth effect."],
  vocab:[["general-knowledge","일반 상식"],["trivia","잡학"],["statement","진술문"],["evaluate","평가하다"],["at ~ intervals","~ 간격으로"],["crucially","결정적으로"],["rating","평가"],["claim","주장"],["as a function of","~에 따라"],["repetition","반복"],["illusory","착각을 일으키는"]] },

{ id:"m40", set:"10월 모의고사", no:"40번", type:"요약문", title:"미역취의 화학 신호",
  q:[
    { ask:"평화로운 지역의 미역취는 공격받을 때 어떻게 신호를 보내나요?", keys:[["친족"],["독특","암호","특유"]],
      model:"가까운 친족만 이해할 수 있는 매우 독특한 화학적 비상 신호를 보낸다(암호화된 속삭임)." },
    { ask:"적대적인 지역의 미역취는 어떻게 다른가요?", keys:[["모든","누구나","널리"],["이해"]],
      model:"그 지역의 모든 미역취가 쉽게 이해하는 화학 신호로 위협을 널리 알린다(확성기)." },
    { ask:"이 연구가 처음으로 확인한 것은?", keys:[["보내는","발신"],["받는","수신"],["이롭","이익"]],
      model:"이런 화학적 교신이 받는 식물뿐 아니라 보내는 식물에게도 이롭다는 것" },
    { ask:"요약문을 직접 완성해 보세요. (적대적 환경의 미역취는 … 신호를 보내 … 에게 이롭다)", keys:[["이해","알아듣"],["발신","보내는"],["수신","받는"]],
      model:"평화로운 지역에 비해 적대적 환경의 미역취는 더 널리 이해될 수 있는 화학 신호를 보내 발신자와 수신자 모두에게 이익을 준다." }
  ],
  flow:["평화로운 지역: 친족만 아는 독특한 신호(속삭임)","적대적 지역: 모두가 아는 신호(확성기)","화학 교신은 보내는 쪽에도 이로움(최초 확인)","힘든 시기엔 혼자 남겨지고 싶지 않음 → 짝·매개자 불러들임","요약: 더 이해하기 쉬운 신호 → 양쪽 모두 이익"],
  key:["Instead of using coded whisper networks, these goldenrods [broadcast] the threat via loudspeaker, so to speak."],
  vocab:[["goldenrod","미역취(식물)"],["threat","위협"],["predator","포식자"],["alarm call","비상 신호"],["specific","독특한, 특유의"],["comprehensible","이해할 수 있는"],["kin","친족"],["hostile","적대적인"],["coded","암호화된"],["broadcast","널리 알리다"],["beneficial","이로운"],["mate with","~와 짝짓기하다"],["pollinator","꽃가루 매개자"],["bring in","불러들이다"],["thereby","그렇게 함으로써"]] },

{ id:"o15a", set:"올림포스 Unit 15", no:"ANALYSIS", type:"문장 삽입", title:"좋은 디자인은 문제 상황의 소통",
  q:[
    { ask:"좋은 디자인은 무엇에 대한 이해에서 시작하나요?", keys:[["심리"],["기술"]],
      model:"심리학과 기술에 대한 이해" },
    { ask:"좋은 디자인이 특히 필요로 하는 소통의 방향은?", keys:[["기계"],["사람"]],
      model:"기계에서 사람으로의 원활한 의사소통" },
    { ask:"소통이 특히 중요한 때는 언제인가요?", keys:[["잘못","문제","오해"]],
      model:"일이 잘못될 때. 잘 될 때 원활하게 작동하게 디자인하는 건 비교적 쉽다." },
    { ask:"디자이너가 집중해야 할 것을 정리해 보세요.", keys:[["잘못","문제"],["계획대로만","뿐 아니라","만이 아니라"]],
      model:"계획대로 될 때뿐만 아니라 일이 잘못되는 경우에 주의를 집중해야 한다. 기계가 문제를 알려 주고 사람이 이해해 해결할 때 큰 만족이 생긴다." }
  ],
  flow:["좋은 디자인 = 심리학 + 기술 이해","기계 → 사람 소통 필요","특히 잘못될 때 소통이 중요","디자이너는 실패 상황에 집중해야","기계가 문제 강조 → 사람이 이해·조치 → 만족"],
  key:["Good design requires good communication, especially from [machine] to [person]."],
  vocab:[["indicate","가리키다, 알려 주다"],["relatively","비교적"],["smoothly","원활하게"],["harmoniously","조화롭게"],["misunderstanding","오해"],["highlight","강조하다"],["satisfaction","만족"]] },

{ id:"o15p1", set:"올림포스 Unit 15", no:"Practice 01", type:"문장 삽입", title:"심리학 글을 평가하는 법",
  q:[
    { ask:"심리학 글의 질을 판단할 때 기준으로 삼으면 안 되는 것 두 가지는?", keys:[["결론"],["동의"]],
      model:"그 주장이 사람에 대해 내리는 결론, 그리고 내가 저자의 생각에 동의하는지 여부" },
    { ask:"훌륭한 심리학자 두 명이 어떻게 할 수 있나요?", keys:[["의견","다르"],["증거","설득"]],
      model:"둘 다 설득력 있는 주장과 타당한 증거를 내면서도 인간 행동의 한 측면에 대해 강하게 의견이 다를 수 있다." },
    { ask:"같은 실험이 다른 결과를 낸다면 그것은 무엇을 뜻하나요?", keys:[["잘못","틀린"],["정보","맥락"]],
      model:"반드시 누군가 잘못 연구했다는 뜻이 아니다. 다른 맥락의 다른 결과는 중요한 정보를 줄 수 있다." },
    { ask:"글의 요지를 한 문장으로 정리해 보세요.", keys:[["의견 차이","다른 결과","의견이 다"],["정보","잘못이 아니"]],
      model:"심리학 연구자들 사이의 의견 차이나 서로 다른 결과는 잘못이 아니라 중요한 정보가 될 수 있다." }
  ],
  flow:["심리학 글의 질은 어떻게 평가?","결론이나 내 동의 여부로 판단 ×","좋은 학자도 강하게 의견이 다를 수 있음","같은 실험·다른 환경 → 다른 결과 가능","다른 결과 = 잘못 ×, 중요한 정보 ○"],
  key:["Different findings from different [contexts] could provide us with important [information]."],
  vocab:[["assess","평가하다"],["argument","주장"],["conclude","결론짓다"],["differ","의견이 다르다"],["aspect","측면"],["convincing","설득력 있는"],["conduct","수행하다"],["setting","환경"],["on the contrary","그와는 반대로"],["finding","연구 결과"],["context","맥락"]] },

{ id:"o15p2", set:"올림포스 Unit 15", no:"Practice 02", type:"문장 삽입", title:"경험주의와 단어의 의미 (Hume)",
  q:[
    { ask:"'보면 믿겠다'는 말이 상기시키는 것은?", keys:[["직접","개인"],["경험"]],
      model:"직접적인 개인의 경험이 무엇을 믿을지 정하는 확실한 방법으로 흔히 받아들여진다는 것" },
    { ask:"현미경 예시는 무엇을 보여 주나요?", keys:[["현미경"],["견해","믿","바뀌"]],
      model:"맨눈에 안 보이는 작은 생물을 믿지 않다가 현미경으로 보게 된 뒤 견해가 바뀌었다." },
    { ask:"이 생각을 단어의 의미에 적용하면 어떤 규정이 되나요?", keys:[["감각"],["의미","무의미"]],
      model:"어떤 단어가 의미를 가지려면 어떤 감각적 경험으로 거슬러 올라갈 수 있어야 하고, 아니면 무의미한 소리이다." },
    { ask:"이 이론을 주장한 사람과 그의 입장을 정리해 보세요.", keys:[["Hume","흄"],["경험","관찰"]],
      model:"스코틀랜드 철학자 David Hume(경험론자): 존재하거나 참인 것에 대한 지식은 세상을 관찰하는 데서만 얻을 수 있다." }
  ],
  flow:["'보면 믿겠다' = 직접 경험이 믿음의 기준","현미경 이전엔 미생물을 안 믿음 → 본 뒤 바뀜","이 강조는 단어 의미 규정으로 확장","단어 의미 = 감각 경험으로 추적 가능해야","Hume의 경험론"],
  key:["If any given word is to have meaning, it must be [traceable] back to some [sensory] experience."],
  vocab:[["emphasis","강조"],["expandable","확장될 수 있는"],["prescribe","규정하다"],["derive","도출하다"],["remark","말, 언급"],["recall","상기시키다"],["first-hand","직접적인"],["fundamental","근본적인"],["invisible","보이지 않는"],["the naked eye","맨눈, 육안"],["microscope","현미경"],["sensory","감각의"],["advocate","주장하다"],["empiricist","경험론자"]] },

{ id:"o15p3", set:"올림포스 Unit 15", no:"Practice 03", type:"문장 삽입", title:"분노 표현은 사회적으로 학습된다",
  q:[
    { ask:"분노와 공격성은 어떤 관계인가요?", keys:[["연결","관련"],["같지 않","동일하지 않","다르"]],
      model:"분명히 연결되어 있지만 같은 것은 아니다. 화내지 않고 공격적이거나, 공격적이지 않고 화낼 수도 있다." },
    { ask:"분노를 일부 심리학자들은 어떻게 보나요?", keys:[["사회"],["구성"]],
      model:"생물학적 기반이 있어도 대부분 사회적으로 구성된 것" },
    { ask:"남녀 예시는 무엇을 보여 주나요?", keys:[["남자","남성"],["권장","배운","학습"]],
      model:"우리 문화에서 남자아이들이 분노를 더 드러내도록 권장되고 남성이 분노 관리 과정을 더 많이 듣는다." },
    { ask:"글의 요지를 한 문장으로 정리해 보세요.", keys:[["표현"],["사회","학습"]],
      model:"분노를 표현하는 정도는 생물학적 차이가 아니라 사회적으로 학습되고 결정된다." }
  ],
  flow:["분노 ≠ 공격성(연결은 됨)","둘 다 생물학적 기반·생존 가치","분노는 사회적으로 구성됨","기질은 달라도 표현 정도는 사회가 결정","남녀 차이 = 학습된 차이"],
  key:["These are [learned] differences, not [biological] ones."],
  vocab:[["burst","폭발"],["construct","구성하다"],["aggression","공격성"],["one and the same","동일한"],["survival value","생존 가치"],["temperamentally","기질적으로"],["be prone to","~하기 쉽다"],["extent","정도"],["determine","결정하다"],["proportion","비율"],["management","관리"]] },

{ id:"o16a", set:"올림포스 Unit 16", no:"ANALYSIS", type:"요약문", title:"온라인 정보 탐색의 위험",
  q:[
    { ask:"온라인 환경이 '시의적절함'에 더한 새로운 차원은?", keys:[["즉시","바로","즉각"]],
      model:"주머니 속 기기로 정보를 즉시 찾을 수 있게 되었다." },
    { ask:"과거와 현재의 정보 탐색을 비교해 보세요.", keys:[["과거","전에는","예전"],["대충","훑","검증"]],
      model:"과거: 접근 가능한 제한된 자료 중 좋은 자료를 공들여 찾음 / 현재: 검증 안 된 무한한 정보를 대충 훑어봄" },
    { ask:"학생들의 어떤 경향 때문에 위험이 커지나요?", keys:[["미루","짧은"]],
      model:"과제를 미루고 가능한 한 가장 짧은 시간에 끝내려는 경향" },
    { ask:"요약문을 직접 만들어 보세요.", keys:[["즉각","즉시","온라인"],["의존","위험"]],
      model:"제한된 자료에서 즉각적인 온라인 접근으로 바뀌며 정보가 넘쳐나게 되었고, 이는 학습을 온라인 자료에만 의존하는 위험을 높인다." }
  ],
  flow:["온라인 환경 → 시의성에 새 차원","예전: 직접 가서 찾아야 함(시간 걸림)","지금: 기기로 즉시 검색","예전 = 공들여 좋은 자료 / 지금 = 검증 안 된 정보 훑기","미루는 경향 + 온라인만 의존 → 위험"],
  key:["The information-rich, online environment has added a new dimension to the area of [timeliness]."],
  vocab:[["dimension","차원"],["timeliness","시의적절함"],["restrict","제한하다"],["repository","저장소"],["let alone","~은 말할 것도 없이"],["painstakingly","공들여"],["skim","대충 훑어보다"],["unvetted","검증되지 않은"],["at lightning speed","빛의 속도로"],["tendency","경향"],["exclusively","오로지"]] },

{ id:"o16p1", set:"올림포스 Unit 16", no:"Practice 01", type:"요약문", title:"부정성 편향",
  q:[
    { ask:"심리학적으로 나쁜 것과 좋은 것 중 무엇이 더 강한가요? 근거 숫자는?", keys:[["나쁜","부정"],["4","네"]],
      model:"나쁜 것이 더 강하다. 부정적인 것 하나의 균형을 맞추려면 긍정적 상호작용 네 번이 필요하다." },
    { ask:"나쁜 기억과 좋은 기억은 어떻게 다르게 남나요?", keys:[["선명","뚜렷","강하"],["흐릿"]],
      model:"나쁜 기억은 강하고 선명한 감정과 함께 또렷이 남고, 좋은 기억은 흐릿하고 전반적인 좋은 느낌만 남는다." },
    { ask:"뇌가 이렇게 발달한 이유는?", keys:[["생존","살아남"],["위험"]],
      model:"위험한 상황을 매우 분명히 기억해 앞으로 피하게 함으로써 생존하도록 진화했기 때문" },
    { ask:"요약문을 직접 완성해 보세요.", keys:[["압도","강하","우세"],["생존"]],
      model:"부정성 편향은 나쁜 경험·기억이 좋은 것을 어떻게 압도하는지, 그리고 뇌가 생존을 위해 왜 이렇게 발달했는지 설명한다." }
  ],
  flow:["나쁜 것 > 좋은 것","부정 1 = 긍정 4","나쁜 기억 선명 / 좋은 기억 흐릿","= 부정성 편향","생존을 위해 위험을 선명히 기억하도록 진화","지금도 뇌는 모든 상황에서 부정적인 것을 찾음"],
  key:["It takes four [positive] interactions to balance out a single [negative] one."],
  vocab:[["psychological","심리학적인"],["balance out","균형을 맞추다"],["resonate","울려 퍼지다"],["vivid","선명한"],["fuzzy","흐릿한"],["edge","끝, 가장자리"],["negativity bias","부정성 편향"],["evolutionary pressure","진화의 압박"],["split off","떨어져 나오다"],["tribe","부족"],["prey","먹이"]] },

{ id:"o16p2", set:"올림포스 Unit 16", no:"Practice 02", type:"요약문", title:"초반 연승과 통제의 착각",
  q:[
    { ask:"1975년 Langer와 Roth의 실험은 무엇이었나요?", keys:[["동전"],["학생","대학생"]],
      model:"스탠퍼드 대학생 90명에게 동전 던지기 결과 맞히기 게임을 하게 했다." },
    { ask:"초반에 잘 맞힌 학생들은 어떻게 생각했나요?", keys:[["예측","맞히"],["정확","실력","할 수 있"]],
      model:"자신이 결과를 정확하게 예측하고 있다고 판단할 가능성이 훨씬 높았다." },
    { ask:"카지노는 이 원리를 어떻게 이용하나요?", keys:[["초반","빠른","연속"],["승리","이길"]],
      model:"결과가 무작위인데도 초반의 빠른 연승이 게임에서 이길 수 있다고 생각하게 만든다." },
    { ask:"요약문을 직접 완성해 보세요.", keys:[["초반","조기"],["통제","예측","무작위가 아니"]],
      model:"초반의 연속적 성공은 사람들에게 미래 결과를 통제(예측)할 수 있다고 생각하게 만들며, 이는 카지노 전략을 설명한다." }
  ],
  flow:["동전 던지기 예측 실험","초반 정답 → '나는 예측할 수 있다'","연승 뒤 패배 = 이해했다고 착각 / 섞인 패배 = 무작위라고 생각","4번 연속 정답이면 똑똑한 사람도 믿음","카지노의 초반 연승 전략"],
  key:["A sequence of wins followed by losses led the students to imagine they [understood] the task."],
  vocab:[["undergraduate","대학생"],["coin flip","동전 던지기"],["predict","예측하다"],["outcome","결과"],["sequence","연속, 순서"],["intermixed","섞인"],["figure out","파악하다"],["random","무작위의"],["hook","걸려들게 하다"],["streak","연속"]] },

{ id:"o16p3", set:"올림포스 Unit 16", no:"Practice 03", type:"요약문", title:"유전성은 상수가 아니다",
  q:[
    { ask:"유전성은 무엇과 다르다고 하나요?", keys:[["상수"],["빛","중력"]],
      model:"빛의 속도나 중력 같은 상수가 아니다." },
    { ask:"유전성의 정확한 뜻은?", keys:[["특정"],["통계"]],
      model:"특정 시기의 특정 집단을 그 집단의 유전·환경 영향의 조합으로 설명하는 통계 자료" },
    { ask:"체중 유전성 예시를 설명해 보세요.", keys:[["부유","잘사는"],["높","크"]],
      model:"부유한 나라(미국)가 가난한 나라보다 체중 유전성이 크다. 살찌는 음식 접근성이 높아 몸무게가 느는 유전적 차이가 드러나기 때문" },
    { ask:"요약문을 직접 완성해 보세요.", keys:[["통계"],["집단","환경","시기"]],
      model:"유전성은 집단(과 시기)에 따라 변하는 유전적·환경적 요인을 반영하는 통계이다." }
  ],
  flow:["유전성은 자주 오해받음","상수 × → 특정 집단·시기의 통계","있는 것을 설명, 있을 수 있는 것은 예측 ×","집단·시기마다 조합이 다름","체중 유전성: 부유한 나라 > 가난한 나라"],
  key:["It describes what [is], but does not [predict] what could be."],
  vocab:[["heritability","유전성"],["frequently","자주"],["constant","상수"],["statistic","통계 자료"],["population","개체군, 집단"],["genetic","유전적인"],["reflect","반영하다"],["access","접근(성)"],["outlet","매장"],["fattening","살찌는"],["propensity","성향"],["put on pounds","살이 찌다"]] },

{ id:"o16w1", set:"올림포스 Unit 16", no:"서술형 (1)(2)", type:"서술형", title:"나이가 삶을 질서화하다",
  q:[
    { ask:"나이가 삶을 질서화하는 기준이 된 것은 언제부터인가요?", keys:[["1800"],["후반"]],
      model:"미국 등에서 1800년대 후반부터, 1900년대 초에 심화" },
    { ask:"나이가 중요해진 배경은?", keys:[["산업화"],["교육","의료","체계"]],
      model:"산업화의 부상과 교육·의료 같은 서비스를 체계화하려는 노력" },
    { ask:"그 전에는 어땠나요?", keys:[["나이","몰랐","알지 못"],["배우","진급"]],
      model:"사람들은 자기 나이를 거의 몰랐고, 학생은 배우는 대로 진급했다." },
    { ask:"글의 요지를 한 문장으로 정리해 보세요.", keys:[["나이","연령"],["중심","질서"]],
      model:"지난 150년 동안 나이 개념과 연령 등급 관행은 현대인의 삶을 질서화하는 데 (흔히 눈에 띄지 않지만) 중심적인 역할을 하게 되었다." }
  ],
  flow:["1800년대 후반부터 나이가 삶의 질서 기준","산업화 + 서비스 체계화 → 나이 = 발달 척도·분류 기준","연령별 기관 등장(발달심리학, 소아과, 연령별 학교)","그 전엔 나이를 거의 몰랐음","지금은 나이가 삶을 질서화하는 중심"],
  key:["It was not until the last half of the 1800s that age became a [criterion] for ordering lives."],
  vocab:[["criterion","기준"],["intensify","심화되다"],["industrialization","산업화"],["systematize","체계화하다"],["sort","분류하다"],["pediatrics","소아과"],["age-graded","연령별의"],["rarely","거의 ~않는"],["refer to","~을 언급하다"],["distinguish","구분하다"],["associated","관련된"],["unnoticed","눈에 띄지 않는"]] },

{ id:"o16w2", set:"올림포스 Unit 16", no:"서술형 (3)", type:"표 완성", title:"기념품의 두 범주",
  q:[
    { ask:"기념품은 어떤 두 그룹으로 나뉘나요?", keys:[["Sampled","샘플","표본"],["Representative","대표"]],
      model:"Sampled(표본형) 기념품과 Representative(대표형) 기념품" },
    { ask:"Sampled 기념품의 특징과 예시는?", keys:[["직접"],["조개","조약돌","꽃"]],
      model:"현지 문화의 개입 없이 여행자가 직접 수집한 개인 경험의 기념품. 예: 조개껍데기, 조약돌, 말린 야생화, 동물 유해" },
    { ask:"Representative 기념품의 특징과 예시는?", keys:[["구매","재현","매개"],["엽서","포스터","달력","토기"]],
      model:"인간의 매개와 해석으로 만들어진 구매 가능한 재현물(인공물). 예: 엽서, 포스터, 달력, 토기, 부메랑" },
    { ask:"두 범주를 가르는 기준을 한 문장으로 정리해 보세요.", keys:[["소비재","구매"],["개인 경험","직접"]],
      model:"일반 소비재로 살 수 없는 개인 경험의 기념품(Sampled)인지, 사람이 만든 구매 가능한 재현물(Representative)인지로 나뉜다." }
  ],
  flow:["기념품 = Sampled vs Representative","Sampled: 소비재로 살 수 없는 개인 경험","여행자가 직접 수집(조개·조약돌·꽃)","Representative: 재현물·구매 가능·인공물","엽서·포스터·달력 / 토기·부메랑"],
  key:["Sampled souvenirs are souvenirs of individual experience that are not available as general [consumer] goods."],
  vocab:[["souvenir","기념품"],["sample","표본을 만들다"],["representative","대표하는"],["binary","이분법적인"],["consumer goods","소비재"],["intervention","개입"],["mediation","중재, 매개"],["pebble","조약돌"],["remains","유해"],["purchasable","구매 가능한"],["artifact","인공물"],["embrace","포함하다"],["clay pot","토기"],["wilderness","미개지"]] },

{ id:"o17a", set:"올림포스 Unit 17", no:"ANALYSIS 01~02", type:"장문", title:"인공 신경망의 시행착오 학습",
  q:[
    { ask:"인공 신경망은 '속담 속 사냥꾼'처럼 어떻게 작동하나요?", keys:[["오류"],["조정"]],
      model:"자신의 오류를 관찰하고, 오류를 가장 잘 줄일 방향으로 내부 상태를 조정한다." },
    { ask:"'지도 학습'과 '오류 역전달'의 뜻을 각각 정리해 보세요.", keys:[["정답"],["되돌려","다시","역"]],
      model:"지도 학습: 감독자처럼 정답을 아는 누군가가 있음 / 오류 역전달: 오류 신호를 망에 다시 보내 매개 변수를 수정함" },
    { ask:"학습 절차 4단계를 순서대로 써 보세요.", keys:[["시도"],["측정"],["조정"]],
      model:"답을 시도 → 정답을 들음 → 오류를 측정 → 오류를 줄이게 매개 변수 조정(조금씩)" },
    { ask:"이 학습이 느릴 수 있는 이유는?", keys:[["수많은","많은"],["매개","반복","수천"]],
      model:"매번 조금씩만 수정하므로 복잡한 활동은 수천~수십억 번 반복해야 하고, 매개 변수가 많아 최적값을 찾는 데 오래 걸린다." }
  ],
  flow:["인공 신경망 = 오류를 보고 내부 조정(사냥꾼)","정답을 정확히 알려 줌 → 지도 학습","오류 신호를 되돌려 수정 → 오류 역전달","시도 → 정답 → 오류 측정 → 조정","조금씩 고치므로 수십억 번 반복 필요(느림)"],
  answer:"01 ⑤ Trial and Error: The Learning Mechanism Behind Artificial Neural Networks",
  key:["They observe their [errors] and use them to adjust their internal state in the direction they believe is best able to [reduce] the error."],
  vocab:[["artificial","인공의"],["neural network","신경망"],["adjustable","조정 가능한"],["parameter","매개 변수"],["proverbial","속담의"],["internal","내부의"],["activate","활성화하다"],["modify","수정하다"],["supervised learning","지도 학습"],["error backpropagation","오류 역전달"],["procedure","절차"],["measure","측정하다"],["recipe","방안"],["a multitude of","수많은"],["optimal","최적의"]] },

{ id:"o17p1", set:"올림포스 Unit 17", no:"Practice 01~02", type:"장문", title:"Pikler: 아이에게 규칙을 평화롭게 알려 주기",
  q:[
    { ask:"Pikler가 먼저 제안하는 것은?", keys:[["안전"],["공간"],["금지"]],
      model:"아이 능력에 맞고 아무것도 금지할 필요가 없는 안전한 놀이 공간을 만드는 것" },
    { ask:"아이가 기어 다니며 공간을 벗어나면 엄마는 무엇을 하나요?", keys:[["규칙","금지"],["반복","말"]],
      model:"규칙과 금지 사항에 주의를 끌게 하고 '안 돼'를 반복해서 말한다. 못 참으면 안전한 공간으로 다시 데려간다." },
    { ask:"이때 아이가 느껴야 하는 것과 느끼면 안 되는 것은?", keys:[["벌"],["신뢰","믿"]],
      model:"벌로 느끼면 안 되고, 엄마가 자신을 신뢰한다고 느껴야 한다('아직 어리지만 시간이 지나면 바뀔 거야')." },
    { ask:"이 방법의 효과를 정리해 보세요.", keys:[["세상","펼쳐"],["위축"]],
      model:"전에 허용하던 것을 갑자기 금지할 때 생기는 위축 대신 세상이 점차 펼쳐지고, 아이는 제한을 점점 이해하고 받아들인다." }
  ],
  flow:["안전하고 금지 없는 공간 만들기","아이가 기어 나가면 엄마와 함께 탐색","규칙·금지에 주의 끌기 + '안 돼' 반복","못 참으면 안전 공간으로(벌 ×, 신뢰 ○)","위축 대신 세상이 펼쳐짐 → 제한을 이해·수용"],
  answer:"01 ⑤ Soft Guidance, Clear Boundaries: Helping Children Understand Rules",
  key:["First, she suggests creating a safe area for the children, suitable for their abilities, where nothing needs to be [prohibited]."],
  vocab:[["illustrate","(예를 들어) 설명하다"],["instructive","교육적인"],["suitable","적합한"],["prohibit","금지하다"],["on all fours","기어서"],["prohibition","금지"],["intriguing","흥미를 자아내는"],["secure","안전한"],["gradually","점차"],["unfold","펼쳐지다"],["shrink","위축되다"],["ban","금지하다"],["limitation","제한"],["gently","온화하게"],["expressly","분명하게"]] },

{ id:"o17p3", set:"올림포스 Unit 17", no:"Practice 03~04", type:"장문", title:"소속되고 싶은 마음과 특별해지고 싶은 마음",
  q:[
    { ask:"우리가 피하고 싶어 하는 것은?", keys:[["다루기 어려운","무리","이상","특이"]],
      model:"다루기 어려운 사람, 무리에 속하지 않는 사람, 특이하거나 반사회적인 사람으로 보이는 것" },
    { ask:"괴짜들의 궁극적인 목표는 무엇인가요?", keys:[["인정"]],
      model:"(소수에게라도) 인정받는 것이지, 완전한 사회적 부랑자가 되는 것이 아니다." },
    { ask:"소속되고 싶은 열망과 함께 우리가 원하는 또 다른 것은?", keys:[["개인","특별","돋보"],["인정","중요"]],
      model:"개인으로 인정받고 중요하게 여겨지는 것. 무리에 섞이기만 하지 않고 돋보이고 싶다." },
    { ask:"글의 요지를 한 문장으로 정리해 보세요.", keys:[["소속","인정","받아들여"],["특별","개인","독특"]],
      model:"사람은 사회에 받아들여지고 소속되기를 원하는 동시에, 조금이라도 특별한 개인으로 인정받기를 원한다." }
  ],
  flow:["우리는 보통이고 어울리고 인정받고 싶다","이상하거나 반사회적으로 보이기 싫다","괴짜조차 궁극적으론 인정받고 싶다","동시에 개인으로 인정받고 돋보이고 싶다","간과되기 싫다 → 조금이라도 특별하다고 느끼고 싶다"],
  answer:"03 ③ The Dual Desire for Acceptance and Individuality",
  key:["However few people, we want to feel, just a little bit, [special]."],
  vocab:[["fit in","어울리다"],["odd","특이한"],["anti-social","반사회적인"],["eccentric","괴짜"],["cultivate","키우다"],["idiosyncrasy","특이성"],["ultimate","궁극적인"],["outcast","부랑자, 따돌림받는 사람"],["yearn","갈망하다"],["strain","부단히 노력하다"],["acknowledge","인정하다"],["significance","중요성"],["overlook","간과하다"],["celebrity","유명 인사"]] }
];
