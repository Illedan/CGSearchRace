import * as utils from '../core/utils.js';
import { api as entityModule } from '../entity-module/GraphicEntityModule.js';

export const api2 = {
  showDebug: false
}

export class DebugModule {
  constructor (assets) {
  }

  static get name () {
    return 'debug'
  }

  updateScene (previousData, currentData, progress) {
    for(let id of this.registrations){
        var entity = entityModule.entities.get(id);
        entity.container.visible = api2.showDebug;
    }
  }

  handleFrameData (frameInfo, newRegistrations) {
    this.registrations = this.registrations.concat(newRegistrations);
    return {...frameInfo}
  }

  generateText(text, size, color, align) {
    var textEl = new PIXI.Text(text, {
      fontSize: Math.round(size / 1.2) + 'px',
      fontFamily: 'Lato',
      fontWeight: 'bold',
      fill: color
    });

    textEl.lineHeight = Math.round(size / 1.2);
    if (align === 'right') {
      textEl.anchor.x = 1;
    } else if (align === 'center') {
      textEl.anchor.x = 0.5;
    }

    return textEl;
  };

  reinitScene (container, canvasData) {
  }

  animateScene (delta) {
  }

  handleGlobalData (players) {
  this.registrations = [];
  }
}